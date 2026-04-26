/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import traincraft.Traincraft;
import traincraft.blocks.TCBlocks;
import traincraft.gui.ContainerGuideBook;
import traincraft.items.TCItems;

import java.util.ArrayList;
import java.util.List;

public class GuideBookScreen extends AbstractContainerScreen<ContainerGuideBook> {
    
    private static final ResourceLocation BOOK_TEXTURE = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/gui/guide_book.png");
    private static final int BOOK_WIDTH = 256;
    private static final int BOOK_HEIGHT = 200;
    
    private int leftPos;
    private int topPos;
    
    // Navigation tabs
    private Button blocksTab;
    private Button itemsTab;
    private Button entitiesTab;
    private Button recipesTab;
    
    // Current category
    private Category currentCategory = Category.BLOCKS;
    
    // Content entries
    private List<Entry> entries = new ArrayList<>();
    private int scrollOffset = 0;
    private static final int MAX_ENTRIES_PER_PAGE = 8;
    
    public GuideBookScreen(ContainerGuideBook menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 200;
        loadEntries();
    }
    
    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - imageWidth) / 2;
        this.topPos = (this.height - imageHeight) / 2;
        
        // Create navigation tabs
        int tabWidth = 50;
        int tabHeight = 20;
        
        this.blocksTab = this.addRenderableWidget(Button.builder(Component.translatable("screen.traincraft.guide_book.blocks"), 
            btn -> switchCategory(Category.BLOCKS))
            .pos(leftPos + 10, topPos - 25)
            .size(tabWidth, tabHeight)
            .build());
        
        this.itemsTab = this.addRenderableWidget(Button.builder(Component.translatable("screen.traincraft.guide_book.items"), 
            btn -> switchCategory(Category.ITEMS))
            .pos(leftPos + 70, topPos - 25)
            .size(tabWidth, tabHeight)
            .build());
        
        this.entitiesTab = this.addRenderableWidget(Button.builder(Component.translatable("screen.traincraft.guide_book.entities"), 
            btn -> switchCategory(Category.ENTITIES))
            .pos(leftPos + 130, topPos - 25)
            .size(tabWidth, tabHeight)
            .build());
        
        this.recipesTab = this.addRenderableWidget(Button.builder(Component.translatable("screen.traincraft.guide_book.recipes"), 
            btn -> switchCategory(Category.RECIPES))
            .pos(leftPos + 190, topPos - 25)
            .size(tabWidth, tabHeight)
            .build());
        
        updateTabStates();
    }
    
    private void switchCategory(Category category) {
        this.currentCategory = category;
        this.scrollOffset = 0;
        loadEntries();
        updateTabStates();
    }
    
    private void updateTabStates() {
        this.blocksTab.active = currentCategory != Category.BLOCKS;
        this.itemsTab.active = currentCategory != Category.ITEMS;
        this.entitiesTab.active = currentCategory != Category.ENTITIES;
        this.recipesTab.active = currentCategory != Category.RECIPES;
    }
    
    private void loadEntries() {
        this.entries.clear();
        
        switch (currentCategory) {
            case BLOCKS:
                // Add block entries
                entries.add(new Entry(Component.translatable("block.traincraft.assembly_table_i"), TCBlocks.ASSEMBLY_TABLE_I.get()));
                entries.add(new Entry(Component.translatable("block.traincraft.assembly_table_ii"), TCBlocks.ASSEMBLY_TABLE_II.get()));
                entries.add(new Entry(Component.translatable("block.traincraft.assembly_table_iii"), TCBlocks.ASSEMBLY_TABLE_III.get()));
                entries.add(new Entry(Component.translatable("block.traincraft.distillery"), TCBlocks.DISTILLERY.get()));
                entries.add(new Entry(Component.translatable("block.traincraft.train_workbench"), TCBlocks.TRAIN_WORKBENCH.get()));
                entries.add(new Entry(Component.translatable("block.traincraft.open_hearth_furnace"), TCBlocks.OPEN_HEARTH_FURNACE.get()));
                entries.add(new Entry(Component.translatable("block.traincraft.signal"), TCBlocks.SIGNAL.get()));
                entries.add(new Entry(Component.translatable("block.traincraft.switch_stand"), TCBlocks.SWITCH_STAND.get()));
                break;
            case ITEMS:
                // Add item entries
                entries.add(new Entry(Component.translatable("item.traincraft.steel_ingot"), TCItems.STEEL_INGOT.get()));
                entries.add(new Entry(Component.translatable("item.traincraft.wrench"), TCItems.WRENCH.get()));
                entries.add(new Entry(Component.translatable("item.traincraft.guide"), TCItems.GUIDE.get()));
                entries.add(new Entry(Component.translatable("item.traincraft.canister"), TCItems.CANISTER.get()));
                entries.add(new Entry(Component.translatable("item.traincraft.connector"), TCItems.CONNECTOR.get()));
                break;
            case ENTITIES:
                // Add entity entries (placeholder)
                entries.add(new Entry(Component.translatable("entity.traincraft.locomotive_steam_small"), ItemStack.EMPTY));
                entries.add(new Entry(Component.translatable("entity.traincraft.freight_cart"), ItemStack.EMPTY));
                entries.add(new Entry(Component.translatable("entity.traincraft.passenger_cart"), ItemStack.EMPTY));
                entries.add(new Entry(Component.translatable("entity.traincraft.bogie"), ItemStack.EMPTY));
                break;
            case RECIPES:
                // Add recipe entries (placeholder)
                entries.add(new Entry(Component.translatable("recipe.traincraft.assembly_table_i"), ItemStack.EMPTY));
                entries.add(new Entry(Component.translatable("recipe.traincraft.assembly_table_ii"), ItemStack.EMPTY));
                entries.add(new Entry(Component.translatable("recipe.traincraft.assembly_table_iii"), ItemStack.EMPTY));
                entries.add(new Entry(Component.translatable("recipe.traincraft.distillery"), ItemStack.EMPTY));
                break;
        }
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        
        // Render book background
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(BOOK_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        
        // Render title
        Component title = Component.translatable("screen.traincraft.guide_book.title");
        guiGraphics.drawString(this.font, title, leftPos + (imageWidth / 2) - (this.font.width(title) / 2), topPos + 10, 0x3F3F3F, false);
        
        // Render category title
        Component categoryTitle = Component.translatable("screen.traincraft.guide_book.category." + currentCategory.name().toLowerCase());
        guiGraphics.drawString(this.font, categoryTitle, leftPos + 10, topPos + 30, 0x3F3F3F, false);
        
        // Render entries
        int yPos = topPos + 50;
        int entriesToShow = Math.min(MAX_ENTRIES_PER_PAGE, entries.size() - scrollOffset);
        
        for (int i = 0; i < entriesToShow; i++) {
            Entry entry = entries.get(i + scrollOffset);
            renderEntry(guiGraphics, entry, leftPos + 15, yPos + (i * 15));
        }
        
        // Render scroll indicator if needed
        if (entries.size() > MAX_ENTRIES_PER_PAGE) {
            String scrollText = (scrollOffset + 1) + "-" + Math.min(scrollOffset + MAX_ENTRIES_PER_PAGE, entries.size()) + " / " + entries.size();
            guiGraphics.drawString(this.font, scrollText, leftPos + imageWidth - 50, topPos + imageHeight - 15, 0x3F3F3F, false);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    }
    
    private void renderEntry(GuiGraphics guiGraphics, Entry entry, int x, int y) {
        // Render item icon
        if (!entry.icon.isEmpty()) {
            guiGraphics.renderItem(entry.icon, x, y);
        }
        
        // Render entry name
        guiGraphics.drawString(this.font, entry.name, x + 20, y + 5, 0x3F3F3F, false);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle entry clicks
        int yPos = topPos + 50;
        int entriesToShow = Math.min(MAX_ENTRIES_PER_PAGE, entries.size() - scrollOffset);
        
        for (int i = 0; i < entriesToShow; i++) {
            int entryY = yPos + (i * 15);
            if (mouseX >= leftPos + 15 && mouseX < leftPos + BOOK_WIDTH - 15 && mouseY >= entryY && mouseY < entryY + 15) {
                // Entry clicked - would show detailed view in a full implementation
                return true;
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (entries.size() > MAX_ENTRIES_PER_PAGE) {
            scrollOffset = Math.max(0, Math.min(entries.size() - MAX_ENTRIES_PER_PAGE, scrollOffset - (int) Math.signum(deltaY)));
        }
        return true;
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    public enum Category {
        BLOCKS,
        ITEMS,
        ENTITIES,
        RECIPES
    }
    
    public static class Entry {
        public final Component name;
        public final ItemStack icon;
        
        public Entry(Component name, ItemStack icon) {
            this.name = name;
            this.icon = icon;
        }
        
        public Entry(Component name, Block block) {
            this.name = name;
            this.icon = new ItemStack(block);
        }
        
        public Entry(Component name, net.minecraft.world.item.Item item) {
            this.name = name;
            this.icon = new ItemStack(item);
        }
    }
}