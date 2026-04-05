/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.distillery;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import traincraft.Traincraft;

public class ScreenDistillery extends AbstractContainerScreen<ContainerDistillery> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/gui/gui_distillation_tower2.png");

    public ScreenDistillery(ContainerDistillery menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        TileDistillery tile = menu.tile;
        if (tile.getMaxBurnTime() > 0) {
            int burnHeight = (int) (13.0f * tile.getBurnTime() / tile.getMaxBurnTime());
            guiGraphics.blit(TEXTURE, x + 58, y + 37 + (13 - burnHeight), 176, 13 - burnHeight, 13, burnHeight);
        }

        if (tile.getMaxRecipeBurnTime() > 0) {
            int progress = (int) (24.0f * (tile.getMaxRecipeBurnTime() - tile.getRecipeBurnTime()) / tile.getMaxRecipeBurnTime());
            guiGraphics.blit(TEXTURE, x + 86, y + 35, 176, 0, progress, 38);
        }

        int fluidAmount = tile.getFluidTank().getFluidAmount();
        int fluidCapacity = tile.getFluidTank().getCapacity();
        if (fluidCapacity > 0 && fluidAmount > 0) {
            int fluidHeight = (int) (52.0f * fluidAmount / fluidCapacity);
            guiGraphics.blit(TEXTURE, x + 144, y + 6 + (52 - fluidHeight), 189, 52 - fluidHeight, 20, fluidHeight);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        TileDistillery tile = menu.tile;
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (mouseX >= x + 144 && mouseX < x + 164 && mouseY >= y + 6 && mouseY < y + 58) {
            var fluid = tile.getFluidTank().getFluid();
            if (!fluid.isEmpty()) {
                int fluidCapacity = tile.getFluidTank().getCapacity();
                guiGraphics.renderTooltip(this.font, Component.translatable(fluid.getTranslationKey())
                    .append(Component.literal(String.format(" (%d / %d mB)", fluid.getAmount(), fluidCapacity))), mouseX, mouseY);
            }
        }
    }
}
