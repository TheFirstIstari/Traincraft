/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class ItemWrench extends Item {

    public ItemWrench() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            var state = context.getLevel().getBlockState(context.getClickedPos());
            var block = state.getBlock();

            if (state.hasProperty(BlockStateProperties.AXIS)) {
                var newAxis = switch (state.getValue(BlockStateProperties.AXIS)) {
                    case X -> net.minecraft.core.Direction.Axis.Y;
                    case Y -> net.minecraft.core.Direction.Axis.Z;
                    case Z -> net.minecraft.core.Direction.Axis.X;
                };
                context.getLevel().setBlock(context.getClickedPos(), state.setValue(BlockStateProperties.AXIS, newAxis), 3);
            } else if (block instanceof RotatedPillarBlock) {
                var newAxis = switch (state.getValue(RotatedPillarBlock.AXIS)) {
                    case X -> net.minecraft.core.Direction.Axis.Y;
                    case Y -> net.minecraft.core.Direction.Axis.Z;
                    case Z -> net.minecraft.core.Direction.Axis.X;
                };
                context.getLevel().setBlock(context.getClickedPos(), state.setValue(RotatedPillarBlock.AXIS, newAxis), 3);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Use to rotate certain blocks."));
    }
}
