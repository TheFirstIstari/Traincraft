/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import traincraft.api.AbstractRollingStock;

import java.util.List;

public class ItemSkinChanger extends Item {

    public ItemSkinChanger() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        Vec3 look = player.getLookAngle();
        Vec3 start = player.getEyePosition(1.0f);
        Vec3 end = start.add(look.scale(5.0));
        AABB box = new AABB(start, end).inflate(1.0);

        List<AbstractRollingStock<?>> entities = level.getEntitiesOfClass(AbstractRollingStock.class, box);
        if (!entities.isEmpty()) {
            AbstractRollingStock<?> rollingStock = entities.get(0);
            if (!level.isClientSide) {
                int nextSkin = rollingStock.getNextSkinId();
                rollingStock.setActiveSkin(nextSkin);
                player.sendSystemMessage(Component.literal("§7Skin changed to: " + nextSkin));
            }
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Right-click near a rolling stock"));
        tooltip.add(Component.literal("§7 to cycle its skin."));
    }
}
