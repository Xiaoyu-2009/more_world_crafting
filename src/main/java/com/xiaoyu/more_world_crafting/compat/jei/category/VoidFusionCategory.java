package com.xiaoyu.more_world_crafting.compat.jei.category;

import java.util.Arrays;

import javax.annotation.Nullable;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.jei.api.JEIDrawHelper;
import com.xiaoyu.more_world_crafting.compat.jei.api.JEIMultiIconUtils;
import com.xiaoyu.more_world_crafting.recipe.VoidFusionRecipe;

public class VoidFusionCategory implements IRecipeCategory<VoidFusionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MoreWorldCrafting.MODID, "void_fusion");
    public static final RecipeType<VoidFusionRecipe> VOID_FUSION_TYPE = new RecipeType<>(UID, VoidFusionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final JEIDrawHelper drawHelper;

    public VoidFusionCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 60);
        this.icon = JEIMultiIconUtils.createVoidIcon(helper);
        this.drawHelper = new JEIDrawHelper(helper);
    }

    @Override
    public RecipeType<VoidFusionRecipe> getRecipeType() {
        return VOID_FUSION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.more_world_crafting.void_fusion");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, VoidFusionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 22)
            .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getIngredient().getItems()));

        builder.addSlot(RecipeIngredientRole.CATALYST, 46, 22)
            .setSlotName("void");
            
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 22)
            .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(VoidFusionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawHelper.drawSlot(guiGraphics, 9, 21);
        drawHelper.drawSlot(guiGraphics, 115, 21);
        
        ResourceLocation voidTexture = new ResourceLocation("minecraft", "textures/entity/end_portal.png");
        guiGraphics.blit(voidTexture, 46, 22, 0, 0, 16, 16, 16, 16);
        
        drawHelper.drawArrow(guiGraphics, 75, 24);
        
        float chance = recipe.getConversionChance();
        int percentage = Math.round(chance * 100);
        Component chanceText = Component.translatable("jei.more_world_crafting.conversion_chance", percentage + "%");
        
        int centerX = (9 + 46 + 16) / 2;
        int textX = centerX - Minecraft.getInstance().font.width(chanceText) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, chanceText, textX, 45, 0x555555, false);
    }
}