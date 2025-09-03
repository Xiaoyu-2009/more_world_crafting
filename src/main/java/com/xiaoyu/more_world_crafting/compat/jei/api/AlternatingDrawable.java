package com.xiaoyu.more_world_crafting.compat.jei.api;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

public class AlternatingDrawable implements IDrawable {
    private final IMultiIconProvider iconProvider;
    
    public AlternatingDrawable(IMultiIconProvider iconProvider) {
        this.iconProvider = iconProvider;
    }
    
    @Override
    public int getWidth() {
        return 16;
    }
    
    @Override
    public int getHeight() {
        return 16;
    }
    
    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        if (iconProvider == null) return;
        
        long currentTime = System.currentTimeMillis() / 50;
        IDrawable currentIcon = iconProvider.getCurrentIcon(currentTime);
        
        if (currentIcon != null) {
            currentIcon.draw(guiGraphics, xOffset, yOffset);
        }
    }
}