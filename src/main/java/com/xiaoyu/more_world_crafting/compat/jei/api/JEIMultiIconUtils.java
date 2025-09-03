package com.xiaoyu.more_world_crafting.compat.jei.api;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class JEIMultiIconUtils {
    public static IDrawable createWaterIcon(IGuiHelper guiHelper) {
        return createWaterIcon(guiHelper, true, 30);
    }

    public static IDrawable createWaterIcon(IGuiHelper guiHelper, boolean alternating, int interval) {
        IMultiIconProvider provider = new MultiIconProvider.Builder(guiHelper)
            .addItemIcon(new ItemStack(Items.WATER_BUCKET))
            .addFluidIcon(new FluidStack(Fluids.WATER, 1000))
            .setAlternating(alternating)
            .setAlternatingInterval(interval)
            .build();
        return new AlternatingDrawable(provider);
    }

    public static IDrawable createLavaIcon(IGuiHelper guiHelper) {
        return createLavaIcon(guiHelper, true, 30);
    }

    public static IDrawable createLavaIcon(IGuiHelper guiHelper, boolean alternating, int interval) {
        IMultiIconProvider provider = new MultiIconProvider.Builder(guiHelper)
            .addItemIcon(new ItemStack(Items.LAVA_BUCKET))
            .addFluidIcon(new FluidStack(Fluids.LAVA, 1000))
            .setAlternating(alternating)
            .setAlternatingInterval(interval)
            .build();
        return new AlternatingDrawable(provider);
    }

    public static IDrawable createVoidIcon(IGuiHelper guiHelper) {
        ResourceLocation endPortalTexture = new ResourceLocation("minecraft", "textures/entity/end_portal.png");
        return guiHelper.drawableBuilder(endPortalTexture, 0, 0, 16, 16).setTextureSize(16, 16).build();
    }

    public static IDrawable createVoidIcon(IGuiHelper guiHelper, boolean alternating, int interval) {
        ResourceLocation endPortalTexture = new ResourceLocation("minecraft", "textures/entity/end_portal.png");
        return guiHelper.drawableBuilder(endPortalTexture, 0, 0, 16, 16).setTextureSize(16, 16).build();
    }

    public static IDrawable createFireIcon(IGuiHelper guiHelper) {
        return createFireIcon(guiHelper, true, 30);
    }

    public static IDrawable createFireIcon(IGuiHelper guiHelper, boolean alternating, int interval) {
        IMultiIconProvider provider = new MultiIconProvider.Builder(guiHelper)
            .addItemIcon(new ItemStack(Items.CAMPFIRE))
            .addItemIcon(new ItemStack(Items.SOUL_CAMPFIRE))
            .setAlternating(alternating)
            .setAlternatingInterval(interval)
            .build();
        return new AlternatingDrawable(provider);
    }

    public static IDrawable createCustomIcon(IGuiHelper guiHelper, ItemStack itemStack, FluidStack fluidStack, boolean alternating, int interval) {
        IMultiIconProvider provider = new MultiIconProvider.Builder(guiHelper)
            .addItemIcon(itemStack)
            .addFluidIcon(fluidStack)
            .setAlternating(alternating)
            .setAlternatingInterval(interval)
            .build();
        return new AlternatingDrawable(provider);
    }

    public static IDrawable createMultiItemIcon(IGuiHelper guiHelper, ItemStack[] itemStacks, boolean alternating, int interval) {
        MultiIconProvider.Builder builder = new MultiIconProvider.Builder(guiHelper);
        for (ItemStack itemStack : itemStacks) {
            builder.addItemIcon(itemStack);
        }
        
        IMultiIconProvider provider = builder
            .setAlternating(alternating)
            .setAlternatingInterval(interval)
            .build();
        return new AlternatingDrawable(provider);
    }

    public static IDrawable createMultiFluidIcon(IGuiHelper guiHelper, FluidStack[] fluidStacks, boolean alternating, int interval) {
        MultiIconProvider.Builder builder = new MultiIconProvider.Builder(guiHelper);
        for (FluidStack fluidStack : fluidStacks) {
            builder.addFluidIcon(fluidStack);
        }
        
        IMultiIconProvider provider = builder
            .setAlternating(alternating)
            .setAlternatingInterval(interval)
            .build();
        return new AlternatingDrawable(provider);
    }
}