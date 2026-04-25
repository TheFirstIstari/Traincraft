/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.network;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import traincraft.Traincraft;
import traincraft.blocks.assemblytable.ContainerAssemblyTable;
import traincraft.blocks.distillery.ContainerDistillery;
import traincraft.blocks.openhearthfurnace.ContainerOpenHearthFurnace;
import traincraft.blocks.trainworkbench.ContainerTrainWorkbench;

public class TCMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Traincraft.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerDistillery>> DISTILLERY = MENUS.register("distillery",
        () -> new MenuType<>(ContainerDistillery::new, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerAssemblyTable>> ASSEMBLY_TABLE = MENUS.register("assembly_table",
        () -> new MenuType<>(ContainerAssemblyTable::new, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerTrainWorkbench>> TRAIN_WORKBENCH = MENUS.register("train_workbench",
        () -> new MenuType<>(ContainerTrainWorkbench::new, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerOpenHearthFurnace>> OPEN_HEARTH_FURNACE = MENUS.register("open_hearth_furnace",
        () -> new MenuType<>(ContainerOpenHearthFurnace::new, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerGuideBook>> GUIDE_BOOK = MENUS.register("guide_book",
        () -> new MenuType<>(ContainerGuideBook::new, FeatureFlags.DEFAULT_FLAGS));
}
