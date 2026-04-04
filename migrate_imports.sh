#!/bin/bash
# Import migration script for Traincraft 1.12 -> 1.21 NeoForge (macOS compatible)

DIR="${1:-.}"

echo "Migrating imports in: $DIR"

# Block imports - use gnu sed on mac if available, else native
gsed -i 's|net\.minecraft\.block\.Block|net.minecraft.world.level.block.Block|g' $DIR/*.java 2>/dev/null || \
sed -i '' 's|net\.minecraft\.block\.Block|net.minecraft.world.level.block.Block|g' $DIR/*.java

sed -i '' 's|net\.minecraft\.block\.state\.IBlockState|net.minecraft.world.level.block.state.BlockState|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.block\.BlockContainer|net.minecraft.world.level.block.BlockEntityBlock|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.block\.material\.Material|net.minecraft.world.level.material.Material|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.block\.Material|net.minecraft.world.level.material.Material|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.block\.SoundType|net.minecraft.world.level.block.SoundType|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.block\.properties\.PropertyBool|net.minecraft.world.level.block.state.BooleanProperty|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.block\.properties\.PropertyDirection|net.minecraft.world.level.block.state.DirectionalProperty|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.block\.state\.BlockStateContainer|net.minecraft.world.level.block.state.StateDefinition|g' $DIR/*.java
sed -i '' 's|BlockHorizontal|net.minecraft.world.level.block.HorizontalDirectionalBlock|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.block\.BlockHorizontal|net.minecraft.world.level.block.HorizontalDirectionalBlock|g' $DIR/*.java

# Item imports
sed -i '' 's|net\.minecraft\.item\.Item|net.minecraft.world.item.Item|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.item\.ItemStack|net.minecraft.world.item.ItemStack|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.item\.ItemBlock|net.minecraft.world.level.block.ItemBlock|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.item\.ItemArmor|net.minecraft.world.item.ArmorItem|g' $DIR/*.java

# NBT imports
sed -i '' 's|net\.minecraft\.nbt\.NBTTagCompound|net.minecraft.nbt.CompoundTag|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.nbt\.NBTBase|net.minecraft.nbt.Tag|g' $DIR/*.java

# World imports
sed -i '' 's|net\.minecraft\.world\.World|net.minecraft.world.level.Level|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.world\.WorldServer|net.minecraft.server.level.ServerLevel|g' $dir/*.java
sed -i '' 's|net\.minecraft\.world\.IBlockAccess|net.minecraft.world.level.BlockGetter|g' $DIR/*.java

# BlockPos
sed -i '' 's|net\.minecraft\.util\.math\.BlockPos|net.minecraft.core.BlockPos|g' $DIR/*.java

# Direction/Enums
sed -i '' 's|net\.minecraft\.util\.EnumFacing|net.minecraft.core.Direction|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.util\.EnumHand|net.minecraft.world.InteractionHand|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.util\.EnumParticleTypes|net.minecraft.core.particles.ParticleType|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.util\.ITickable|net.minecraft.util.ServerTickable|g' $DIR/*.java

# Entity imports
sed -i '' 's|net\.minecraft\.entity\.Entity|net.minecraft.world.entity.Entity|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.entity\.EntityLivingBase|net.minecraft.world.entity.LivingEntity|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.entity\.player\.EntityPlayer|net.minecraft.world.entity.player.Player|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.entity\.player\.EntityPlayerMP|net.minecraft.server.level.ServerPlayer|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.entity\.item\.EntityItem|net.minecraft.world.entity.item.ItemEntity|g' $DIR/*.java

# TileEntity
sed -i '' 's|net\.minecraft\.tileentity\.TileEntity|net.minecraft.world.level.block.entity.BlockEntity|g' $DIR/*.java

# Container/GUI
sed -i '' 's|net\.minecraft\.inventory\.Container|net.minecraft.world.inventory.AbstractContainerMenu|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.inventory\.IInventory|net.minecraft.world.Container|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.client\.gui\.GuiScreen|net.minecraft.client.gui.screens.Screen|g' $DIR/*.java

# Misc
sed -i '' 's|net\.minecraft\.util\.ResourceLocation|net.minecraft.resources.ResourceLocation|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.util\.NonNullList|net.minecraft.core.NonNullList|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.creativetab\.CreativeTabs|net.minecraft.world.item.CreativeModeTab|g' $DIR/*.java
sed -i '' 's|net\.minecraft\.client\.renderer\.block\.model\.ModelResourceLocation|net.minecraft.client.resources.model.ModelResourceLocation|g' $DIR/*.java

# Forge -> NeoForge
sed -i '' 's|net\.forgeforge\.fml\.common\.Mod|net.neoforged.fml.common.Mod|g' $DIR/*.java
sed -i '' 's|net\.forgeforge\.fml\.common\.event\.FMLPreInitializationEvent|net.neoforged.fml.event.lifecycle.FMLPreInitializationEvent|g' $DIR/*.java
sed -i '' 's|net\.forgeforge\.fml\.common\.eventhandler\.SubscribeEvent|net.neoforged.bus.api.Subscribe|g' $DIR/*.java
sed -i '' 's|net\.forgeforge\.forge\.registries\.ForgeRegistries|net.neoforged.neoforge.registries.ForgeRegistries|g' $DIR/*.java
sed -i '' 's|net\.forgeforge\.forge\.fml\.common\.registry\.GameRegistry|net.neoforged.neoforge.registries.GameRegistry|g' $DIR/*.java

# Fluids
sed -i '' 's|net\.forgeforge\.fluids|net.neoforged.neoforge.fluids|g' $DIR/*.java

# Energy
sed -i '' 's|net\.forgeforge\.energy|net.neoforged.neoforge.energy|g' $DIR/*.java

# Items
sed -i '' 's|net\.forgeforge\.items|net.neoforged.neoforge.items|g' $DIR/*.java

# Common capabilities
sed -i '' 's|net\.forgeforge\.forge\.capabilities|net.neoforged.neoforge.capabilities|g' $DIR/*.java

echo "Done!"