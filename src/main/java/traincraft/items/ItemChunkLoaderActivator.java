/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemChunkLoaderActivator extends Item {

    private static final Set<ChunkPos> loadedChunks = new HashSet<>();

    public ItemChunkLoaderActivator() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ChunkPos chunkPos = new ChunkPos(player.blockPosition());
            ChunkAccess chunk = level.getChunk(chunkPos.x, chunkPos.z);
            if (loadedChunks.contains(chunkPos)) {
                loadedChunks.remove(chunkPos);
                chunk.setUnsaved(true);
                player.sendSystemMessage(Component.literal("§7Chunk force removed."));
            } else {
                loadedChunks.add(chunkPos);
                level.getChunk(chunkPos.x, chunkPos.z).setInhabitedTime(Long.MAX_VALUE);
                player.sendSystemMessage(Component.literal("§7Chunk force added."));
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Right-click to toggle chunk force loading."));
    }

    public static Set<ChunkPos> getLoadedChunks() {
        return loadedChunks;
    }
}
