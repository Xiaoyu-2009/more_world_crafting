package com.xiaoyu.more_world_crafting.compat.jei.api;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class JEIDrawHelper {
    
    private final IGuiHelper guiHelper;
    private final IDrawable slotDrawable;
    private final IDrawable arrowDrawable;
    
    public JEIDrawHelper(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.slotDrawable = guiHelper.getSlotDrawable();
        this.arrowDrawable = guiHelper.drawableBuilder(
            new ResourceLocation("minecraft", "textures/gui/container/furnace.png"), 
            79, 35, 24, 17
        ).build();
    }

    public IDrawable createBackground(int width, int height) {
        return guiHelper.createBlankDrawable(width, height);
    }

    public void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        slotDrawable.draw(guiGraphics, x, y);
    }

    public void drawArrow(GuiGraphics guiGraphics, int x, int y) {
        arrowDrawable.draw(guiGraphics, x, y);
    }

    public void drawItem(GuiGraphics guiGraphics, ItemStack itemStack, int x, int y) {
        if (!itemStack.isEmpty()) {
            guiGraphics.renderItem(itemStack, x, y);
        }
    }

    public void drawInputSlots(GuiGraphics guiGraphics, int startX, int startY, int slotsCount, int maxColumns) {
        for (int i = 0; i < slotsCount; i++) {
            int x = startX + (i % maxColumns) * 18;
            int y = startY + (i / maxColumns) * 18;
            drawSlot(guiGraphics, x, y);
        }
    }

    public int[] getInputSlotPosition(int index, int startX, int startY, int maxColumns) {
        int x = startX + (index % maxColumns) * 18;
        int y = startY + (index / maxColumns) * 18;
        return new int[]{x, y};
    }

    public int[] calculateSize(int maxInputs, int maxColumns) {
        int columns = Math.min(maxInputs, maxColumns);
        int rows = (int) Math.ceil((double) maxInputs / maxColumns);
        int baseWidth = 20 + (columns * 18) + 20 + 18 + 10 + 24 + 10 + 18 + 20;
        int baseHeight = 20 + (rows * 18) + 40;
        return new int[]{Math.max(baseWidth, 180), Math.max(baseHeight, 80)};
    }
}