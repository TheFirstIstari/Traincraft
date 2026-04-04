#!/bin/bash
# Comprehensive import migration for Traincraft 1.12 -> 1.21 NeoForge

find src/main/java/traincraft -name "*.java" -exec sed -i '' \
    -e 's|net\.minecraft\.block\.material\.Material|net.minecraft.world.level.material.Material|g' \
    -e 's|net\.minecraft\.block\.material\.MapColor|net.minecraft.world.level.material.MapColor|g' \
    -e 's|net\.minecraft\.item\.crafting\.Ingredient|net.minecraft.world.item.crafting.Ingredient|g' \
    -e 's|net\.minecraft\.item\.crafting\.IRecipe|net.minecraft.world.item.crafting.Recipe|g' \
    -e 's|net\.minecraft\.item\.ItemStack|net.minecraft.world.item.ItemStack|g' \
    -e 's|net\.minecraft\.item\.Item|net.minecraft.world.item.Item|g' \
    -e 's|net\.minecraft\.client\.renderer\.ItemRenderer|net.minecraft.client.renderer.ItemRenderer|g' \
    -e 's|net\.minecraft\.client\.renderer\.blockmodel\.ModelResourceLocation|net.minecraft.client.resources.model.ModelResourceLocation|g' \
    -e 's|net\.minecraft\.world\.storage\.WorldSavedData|net.minecraft.server.packs.resources.ResourceManager|g' \
    -e 's|net\.minecraft\.server\.management\.PlayerChunkMapEntry|net.minecraft.server.level.ChunkMap|g' \
    -e 's|net\.minecraft\.network\.NetworkManager|net.minecraft.network.Connection|g' \
    -e 's|net\.minecraft\.network\.play\.server\.SPacketUpdateTileEntity|net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket|g' \
    {} \;

echo "Done!"