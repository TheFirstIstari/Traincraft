/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

/**
 * A handheld fluid container.
 * <p>
 * Stores a {@link FluidStack} in the item's CUSTOM_DATA component. Right-clicking a fluid source block
 * picks the fluid up if the canister is empty or already holds the same fluid; sneak + right-click
 * places the stored fluid back into the world (consuming the canister's contents).
 */
public class ItemCanister extends Item {

    public static final int CAPACITY = 16000;
    public static final int FILL_PER_USE = 1000;
    private static final String FLUID_TAG = "stored_fluid";
    private static final String AMOUNT_TAG = "stored_amount";

    public ItemCanister() {
        super(new Properties().stacksTo(1));
    }

    /** Returns the fluid currently stored, or empty. */
    public static FluidStack getStored(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return FluidStack.EMPTY;
        CompoundTag tag = data.copyTag();
        if (!tag.contains(FLUID_TAG, 8)) return FluidStack.EMPTY;
        ResourceLocation id = ResourceLocation.tryParse(tag.getString(FLUID_TAG));
        if (id == null) return FluidStack.EMPTY;
        Fluid fluid = BuiltInRegistries.FLUID.get(id);
        int amount = tag.getInt(AMOUNT_TAG);
        if (fluid == null || amount <= 0) return FluidStack.EMPTY;
        return new FluidStack(fluid, amount);
    }

    /** Persists the supplied fluid back into the item's CUSTOM_DATA. */
    public static void setStored(ItemStack stack, FluidStack fluid) {
        if (fluid.isEmpty()) {
            // Strip the stored-fluid keys without dropping any other custom data.
            stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
                CompoundTag tag = data.copyTag();
                tag.remove(FLUID_TAG);
                tag.remove(AMOUNT_TAG);
                return CustomData.of(tag);
            });
            return;
        }
        ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid.getFluid());
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
            CompoundTag tag = data.copyTag();
            tag.putString(FLUID_TAG, id.toString());
            tag.putInt(AMOUNT_TAG, Math.min(fluid.getAmount(), CAPACITY));
            return CustomData.of(tag);
        });
    }

    /** Try to drain {@code amount} mB of {@code fluid} into the canister. Returns the actually filled amount. */
    public static int fill(ItemStack stack, FluidStack incoming, int amount) {
        FluidStack stored = getStored(stack);
        if (!stored.isEmpty() && stored.getFluid() != incoming.getFluid()) return 0;
        int currentAmount = stored.isEmpty() ? 0 : stored.getAmount();
        int space = CAPACITY - currentAmount;
        int filled = Math.min(amount, space);
        if (filled <= 0) return 0;
        FluidStack newStack = new FluidStack(incoming.getFluid(), currentAmount + filled);
        setStored(stack, newStack);
        return filled;
    }

    /** Drain up to {@code amount} mB from the canister and return what was removed. */
    public static FluidStack drain(ItemStack stack, int amount) {
        FluidStack stored = getStored(stack);
        if (stored.isEmpty()) return FluidStack.EMPTY;
        int taken = Math.min(stored.getAmount(), amount);
        int remaining = stored.getAmount() - taken;
        FluidStack drained = new FluidStack(stored.getFluid(), taken);
        if (remaining <= 0) {
            setStored(stack, FluidStack.EMPTY);
        } else {
            setStored(stack, new FluidStack(stored.getFluid(), remaining));
        }
        return drained;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Sneak + right-click in air with stored fluid -> empty the canister entirely.
        // (We don't try to place a flowing fluid block to avoid stranding partial buckets.)
        if (player.isShiftKeyDown()) {
            FluidStack stored = getStored(stack);
            if (!stored.isEmpty()) {
                if (!level.isClientSide) {
                    setStored(stack, FluidStack.EMPTY);
                    player.displayClientMessage(Component.translatable("item.traincraft.canister.emptied"), true);
                }
                return InteractionResultHolder.success(stack);
            }
        }

        // Right-click on a fluid source -> pick it up.
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hit.getType() != BlockHitResult.Type.MISS) {
            BlockPos pos = hit.getBlockPos();
            BlockState bs = level.getBlockState(pos);
            FluidState fs = level.getFluidState(pos);
            if (!fs.isEmpty() && fs.isSource() && bs.getBlock() instanceof LiquidBlock) {
                FluidStack picked = new FluidStack(fs.getType(), FILL_PER_USE);
                int filled = fill(stack, picked, FILL_PER_USE);
                if (filled > 0) {
                    if (!level.isClientSide) {
                        // Consume the source block (mirroring vanilla bucket behaviour).
                        level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 11);
                    }
                    return InteractionResultHolder.success(stack);
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        FluidStack stored = getStored(stack);
        if (stored.isEmpty()) {
            tooltip.add(Component.translatable("item.traincraft.canister.empty").withStyle(net.minecraft.ChatFormatting.GRAY));
        } else {
            tooltip.add(stored.getHoverName().copy()
                .append(": " + stored.getAmount() + " / " + CAPACITY + " mB")
                .withStyle(net.minecraft.ChatFormatting.AQUA));
        }
        tooltip.add(Component.translatable("item.traincraft.canister.hint").withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
    }
}
