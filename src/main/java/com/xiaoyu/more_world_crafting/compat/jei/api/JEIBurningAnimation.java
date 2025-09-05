package com.xiaoyu.more_world_crafting.compat.jei.api;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.xiaoyu.more_world_crafting.recipe.FireConversionRecipe;

public class JEIBurningAnimation {
    public static IDrawable create(IGuiHelper helper, FireConversionRecipe.FireType fireType) {
        switch (fireType) {
            case SOUL_FIRE:
                return createSoulFireAnimation(helper);
            case CAMPFIRE:
                return helper.createDrawableItemStack(new ItemStack(Items.CAMPFIRE));
            case SOUL_CAMPFIRE:
                return helper.createDrawableItemStack(new ItemStack(Items.SOUL_CAMPFIRE));
            default:
                return createNormalFireAnimation(helper);
        }
    }
    
    private static IDrawable createNormalFireAnimation(IGuiHelper helper) {
        return new IDrawable() {
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
                Minecraft minecraft = Minecraft.getInstance();
                TextureAtlas textureAtlas = minecraft.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
                TextureAtlasSprite fireSprite = textureAtlas.getSprite(new ResourceLocation("minecraft", "block/fire_0"));
                
                guiGraphics.blit(xOffset, yOffset, 0, 16, 16, fireSprite);
            }
        };
    }
    
    private static IDrawable createSoulFireAnimation(IGuiHelper helper) {
        return new IDrawable() {
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
                Minecraft minecraft = Minecraft.getInstance();
                TextureAtlas textureAtlas = minecraft.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
                TextureAtlasSprite soulFireSprite = textureAtlas.getSprite(new ResourceLocation("minecraft", "block/soul_fire_0"));
                
                guiGraphics.blit(xOffset, yOffset, 0, 16, 16, soulFireSprite);
            }
        };
    }
}