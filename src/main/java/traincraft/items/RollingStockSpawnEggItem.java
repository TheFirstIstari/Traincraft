/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class RollingStockSpawnEggItem extends Item {

    private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            EntityType<?> entityType = ((RollingStockSpawnEggItem) stack.getItem()).getType(stack.getTag());
            ServerLevel serverlevel = source.level();

            try {
                entityType.spawn(serverlevel, stack, null, source.pos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
            } catch (Exception e) {
                return this.defaultDispenseItemBehavior.dispense(source, stack);
            }

            stack.shrink(1);
            return stack;
        }
    };

    private final Supplier<? extends EntityType<?>> typeSupplier;

    public RollingStockSpawnEggItem(Supplier<? extends EntityType<?>> typeSupplier, Item.Properties properties) {
        super(properties);
        this.typeSupplier = typeSupplier;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemstack = context.getItemInHand();
        ServerLevel serverlevel = context.getLevel();

        if (!serverlevel.isClientSide) {
            EntityType<?> entitytype = this.getType(itemstack.getTag());
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            boolean flag = serverlevel.getBlockState(blockpos).is(BlockTags.SPAWNS_INSIDE);
            BlockPos blockpos1 = flag ? blockpos : blockpos.relative(direction);
            Entity entity = entitytype.create(serverlevel, itemstack, context.getPlayer(), blockpos1, MobSpawnType.SPAWN_EGG, true, !flag && direction == Direction.UP);

            if (entity == null) {
                return InteractionResult.PASS;
            }

            if (context.getPlayer() != null) {
                entity.spawnAnim();
            }

            serverlevel.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockpos1);
            itemstack.shrink(1);
        }

        context.getPlayer().awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS;
    }

    @Nullable
    public EntityType<?> getType(@Nullable net.minecraft.nbt.CompoundTag tag) {
        return this.typeSupplier.get();
    }
}
