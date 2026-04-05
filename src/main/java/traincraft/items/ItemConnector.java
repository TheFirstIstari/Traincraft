/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import traincraft.api.AbstractRollingStock;

import java.util.List;

public class ItemConnector extends Item {

    private static final String ROLLING_STOCK_ID_KEY = "rolling_stock_id";

    public ItemConnector() {
        super(new Properties().stacksTo(1).durability(200));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && hasRollingStockOnStack(stack)) {
            removeRollingStockFromStack(stack);
            player.sendSystemMessage(Component.literal("Connector cleared."));
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return hasRollingStockOnStack(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Right-click on a rolling stock"));
        tooltip.add(Component.literal("§7 to enter attaching mode."));
        tooltip.add(Component.literal("§7Sneak+Right-click to clear."));
        if (hasRollingStockOnStack(stack)) {
            tooltip.add(Component.literal("§aLinked to a rolling stock"));
        }
    }

    private static CompoundTag getDataTag(ItemStack stack) {
        CustomData customData = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
        if (customData != null) {
            return customData.copyTag();
        }
        return new CompoundTag();
    }

    private static void setDataTag(ItemStack stack, CompoundTag tag) {
        stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static boolean hasRollingStockOnStack(ItemStack stack) {
        CompoundTag tag = getDataTag(stack);
        return tag.contains(ROLLING_STOCK_ID_KEY);
    }

    public static void putRollingStockOnStack(ItemStack stack, AbstractRollingStock<?> rollingStock) {
        CompoundTag tag = getDataTag(stack);
        tag.putInt(ROLLING_STOCK_ID_KEY, rollingStock.getId());
        setDataTag(stack, tag);
    }

    public static void removeRollingStockFromStack(ItemStack stack) {
        CompoundTag tag = getDataTag(stack);
        tag.remove(ROLLING_STOCK_ID_KEY);
        setDataTag(stack, tag);
    }

    public static AbstractRollingStock<?> getRollingStockFromStack(ItemStack stack, Level level) {
        if (hasRollingStockOnStack(stack)) {
            CompoundTag tag = getDataTag(stack);
            int entityId = tag.getInt(ROLLING_STOCK_ID_KEY);
            Entity entity = level.getEntity(entityId);
            if (entity instanceof AbstractRollingStock<?> rollingStock) {
                return rollingStock;
            }
        }
        return null;
    }

    public static void handleEntityClick(AbstractRollingStock<?> rollingStock, Player player, InteractionHand hand, ItemStack connectorStack) {
        if (hasRollingStockOnStack(connectorStack)) {
            AbstractRollingStock<?> other = getRollingStockFromStack(connectorStack, player.level());
            if (other != null && other != rollingStock) {
                if (other.canLinkToAnotherRollingStock(rollingStock, player) &&
                    rollingStock.canLinkToAnotherRollingStock(other, player)) {
                    other.linkToAnotherRollingStock(rollingStock, player);
                    rollingStock.linkToAnotherRollingStock(other, player);
                    removeRollingStockFromStack(connectorStack);
                    player.sendSystemMessage(Component.literal("Connection established."));
                } else {
                    player.sendSystemMessage(Component.literal("These rolling stocks can't be connected."));
                }
            } else {
                removeRollingStockFromStack(connectorStack);
                player.sendSystemMessage(Component.literal("The saved rolling stock was invalid."));
            }
        } else {
            putRollingStockOnStack(connectorStack, rollingStock);
            player.sendSystemMessage(Component.literal("Rolling stock saved. Click another to link."));
        }
    }
}
