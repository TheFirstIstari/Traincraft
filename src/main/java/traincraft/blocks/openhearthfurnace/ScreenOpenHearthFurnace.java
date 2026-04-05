/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.openhearthfurnace;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import traincraft.Traincraft;

public class ScreenOpenHearthFurnace extends AbstractContainerScreen<ContainerOpenHearthFurnace> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/gui/gui_open_hearth_furnace.png");

    public ScreenOpenHearthFurnace(ContainerOpenHearthFurnace menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        TileOpenHearthFurnace tile = menu.tile;
        if (tile.getMaxBurnTime() > 0) {
            int burnHeight = (int) (13.0f * tile.getBurnTime() / tile.getMaxBurnTime());
            guiGraphics.blit(TEXTURE, x + 58, y + 37 + (13 - burnHeight), 176, 13 - burnHeight, 14, burnHeight);
        }

        if (tile.getMaxCookTime() > 0) {
            int progress = (int) (24.0f * tile.getCookTime() / tile.getMaxCookTime());
            guiGraphics.blit(TEXTURE, x + 86, y + 35, 176, 26, progress, 17);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
