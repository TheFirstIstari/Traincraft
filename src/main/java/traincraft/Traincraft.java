/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraft.world.item.CreativeModeTabs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import traincraft.blocks.TCBlocks;
import traincraft.blocks.assemblytable.ScreenAssemblyTable;
import traincraft.blocks.distillery.ScreenDistillery;
import traincraft.blocks.openhearthfurnace.ScreenOpenHearthFurnace;
import traincraft.blocks.trainworkbench.ScreenTrainWorkbench;
import traincraft.gui.GuideBookScreen;
import traincraft.entity.TCEntities;
import traincraft.items.TCItems;
import traincraft.items.armor.TCArmor;
import traincraft.liquids.TCLiquids;
import traincraft.network.TCMenus;
import traincraft.recipe.DistilleryRecipe;
import traincraft.recipe.TCRecipes;
import traincraft.renderer.EntityFreightCartRenderer;
import traincraft.renderer.EntityBogieRenderer;
import traincraft.renderer.LocomotiveSteamSmallRenderer;
import traincraft.renderer.EntityPassengerCartRenderer;
import traincraft.tile.TCTiles;
import traincraft.TCSounds;

@Mod(Traincraft.MOD_ID)
public class Traincraft {

    public static final String MOD_ID = "traincraft";
    public static final String MOD_NAME = "Traincraft";
    public static final String MOD_VERSION = "5.0.0-alpha2";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public Traincraft(IEventBus modEventBus) {
        TCBlocks.BLOCKS.register(modEventBus);
        TCItems.ITEMS.register(modEventBus);
        TCArmor.ARMOR_ITEMS.register(modEventBus);
        TCLiquids.FLUID_TYPES.register(modEventBus);
        TCLiquids.FLUIDS.register(modEventBus);
        TCTiles.TILES.register(modEventBus);
        TCEntities.ENTITIES.register(modEventBus);
        TCMenus.MENUS.register(modEventBus);
        TCRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        TCRecipes.RECIPE_TYPES.register(modEventBus);
        TCSounds.SOUND_EVENTS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreativeTabContents);
        modEventBus.addListener(this::registerScreens);
        modEventBus.addListener(this::registerEntityRenderers);

        LOGGER.info("Traincraft {} loaded!", MOD_VERSION);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup complete");
    }

    private void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            TCItems.ITEMS.getEntries().forEach(holder -> event.accept(holder.get()));
            TCArmor.ARMOR_ITEMS.getEntries().forEach(holder -> event.accept(holder.get()));
        }
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TCMenus.DISTILLERY.get(), ScreenDistillery::new);
        event.register(TCMenus.ASSEMBLY_TABLE.get(), ScreenAssemblyTable::new);
        event.register(TCMenus.TRAIN_WORKBENCH.get(), ScreenTrainWorkbench::new);
        event.register(TCMenus.OPEN_HEARTH_FURNACE.get(), ScreenOpenHearthFurnace::new);
        event.register(TCMenus.GUIDE_BOOK.get(), GuideBookScreen::new);
    }

    private void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TCEntities.LOCOMOTIVE_STEAM_SMALL.get(), LocomotiveSteamSmallRenderer::new);
        event.registerEntityRenderer(TCEntities.LOCOMOTIVE_DIESEL_SMALL.get(), traincraft.renderer.LocomotiveDieselSmallRenderer::new);
        event.registerEntityRenderer(TCEntities.FREIGHT_CART.get(), EntityFreightCartRenderer::new);
        event.registerEntityRenderer(TCEntities.PASSENGER_CART.get(), EntityPassengerCartRenderer::new);
        event.registerEntityRenderer(TCEntities.BOGIE.get(), EntityBogieRenderer::new);
    }
}
