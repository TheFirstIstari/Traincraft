/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.assemblytable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import traincraft.Traincraft;

public class ScreenAssemblyTable extends AbstractContainerScreen<ContainerAssemblyTable> {

    private static final ResourceLocation[] GUI_TEXTURES = {
        ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/gui/gui_assemblytable_tier1.png"),
        ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/gui/gui_assemblytable_tier2.png"),
        ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/gui/gui_assemblytable_tier3.png"),
    };

    private final int tier;

    public ScreenAssemblyTable(ContainerAssemblyTable menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.tier = menu.tile != null ? menu.tile.getTier() : 1;
        this.imageWidth = 176;
        this.imageHeight = 256;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        ResourceLocation texture = GUI_TEXTURES[Math.min(tier - 1, GUI_TEXTURES.length - 1)];
        guiGraphics.blit(texture, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 10, 6, 12241200, false);
        guiGraphics.drawString(this.font, Component.translatable("itemGroup.traincraft"), 10, 118, 9671070, false);
        guiGraphics.drawString(this.font, Component.literal("Output"), 90, 118, 9671070, false);
    }
}
