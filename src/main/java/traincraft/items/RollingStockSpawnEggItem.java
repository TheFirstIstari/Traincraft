/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;
import java.util.function.Supplier;

public class RollingStockSpawnEggItem extends Item {

    private final Supplier<? extends EntityType<?>> typeSupplier;

    public RollingStockSpawnEggItem(Supplier<? extends EntityType<?>> typeSupplier, Item.Properties properties) {
        super(properties);
        this.typeSupplier = typeSupplier;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack itemstack = context.getItemInHand();
        BlockPos blockpos = context.getClickedPos();
        EntityType<?> entitytype = this.typeSupplier.get();

        Entity entity = entitytype.create(level);

        if (entity == null) {
            return InteractionResult.PASS;
        }

        entity.moveTo(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5, entity.getYRot(), entity.getXRot());
        level.addFreshEntity(entity);
        level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
        itemstack.shrink(1);

        if (context.getPlayer() != null) {
            context.getPlayer().awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Spawns a steam locomotive."));
    }
}
