package com.xiaoyu.more_world_crafting.compat.jei.api;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class MultiIconProvider implements IMultiIconProvider {
    private final List<IDrawable> icons;
    private final boolean alternating;
    private final int alternatingInterval;
    
    private MultiIconProvider(List<IDrawable> icons, boolean alternating, int alternatingInterval) {
        this.icons = new ArrayList<>(icons);
        this.alternating = alternating;
        this.alternatingInterval = alternatingInterval;
    }
    
    @Override
    public List<IDrawable> getIcons() {
        return new ArrayList<>(icons);
    }
    
    @Override
    public IDrawable getCurrentIcon(long tickCount) {
        if (icons.isEmpty()) return null;
        if (!alternating || icons.size() == 1) return icons.get(0);
        
        int index = (int) ((tickCount / alternatingInterval) % icons.size());
        return icons.get(index);
    }
    
    @Override
    public boolean isAlternating() {
        return alternating;
    }
    
    @Override
    public int getAlternatingInterval() {
        return alternatingInterval;
    }
    
    public static class Builder {
        private final List<IDrawable> icons = new ArrayList<>();
        private final IGuiHelper guiHelper;
        private boolean alternating = true;
        private int alternatingInterval = 30;
        
        public Builder(IGuiHelper guiHelper) {
            this.guiHelper = guiHelper;
        }
        
        public Builder addItemIcon(ItemStack itemStack) {
            icons.add(guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, itemStack));
            return this;
        }
        
        public Builder addFluidIcon(FluidStack fluidStack) {
            icons.add(guiHelper.createDrawableIngredient(ForgeTypes.FLUID_STACK, fluidStack));
            return this;
        }
        
        public Builder setAlternating(boolean alternating) {
            this.alternating = alternating;
            return this;
        }
        
        public Builder setAlternatingInterval(int intervalTicks) {
            this.alternatingInterval = intervalTicks;
            return this;
        }
        
        public MultiIconProvider build() {
            if (icons.isEmpty()) {
                throw new IllegalStateException("At least one icon needs to be added.");
            }
            return new MultiIconProvider(icons, alternating, alternatingInterval);
        }
    }
}