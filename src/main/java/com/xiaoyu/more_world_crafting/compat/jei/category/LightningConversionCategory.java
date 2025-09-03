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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.jei.api.JEIDrawHelper;
import com.xiaoyu.more_world_crafting.recipe.LightningConversionRecipe;

public class LightningConversionCategory implements IRecipeCategory<LightningConversionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MoreWorldCrafting.MODID, "lightning_conversion");
    public static final RecipeType<LightningConversionRecipe> LIGHTNING_CONVERSION_TYPE = new RecipeType<>(UID, LightningConversionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final JEIDrawHelper drawHelper;

    public LightningConversionCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 70);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.LIGHTNING_ROD));
        this.drawHelper = new JEIDrawHelper(helper);
    }

    @Override
    public RecipeType<LightningConversionRecipe> getRecipeType() {
        return LIGHTNING_CONVERSION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.more_world_crafting.lightning_conversion");
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
    public void setRecipe(IRecipeLayoutBuilder builder, LightningConversionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 22)
            .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getIngredient().getItems()));

        builder.addSlot(RecipeIngredientRole.CATALYST, 46, 22)
            .setSlotName("lightning");

        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 22)
            .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(LightningConversionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawHelper.drawSlot(guiGraphics, 9, 21);
        drawHelper.drawSlot(guiGraphics, 115, 21);

        guiGraphics.renderItem(new ItemStack(Items.LIGHTNING_ROD), 46, 22);

        drawHelper.drawArrow(guiGraphics, 75, 24);

        float lightningChance = recipe.getLightningChance();
        int lightningPercentage = Math.round(lightningChance * 100);
        Component attractText = Component.translatable("jei.more_world_crafting.lightning_absorption_chance", lightningPercentage + "%");
        
        int centerX = (9 + 50 + 16) / 2;
        int attractTextX = centerX - Minecraft.getInstance().font.width(attractText) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, attractText, attractTextX, 45, 0x555555, false);
        
        float conversionChance = recipe.getConversionChance();
        int conversionPercentage = Math.round(conversionChance * 100);
        Component conversionText = Component.translatable("jei.more_world_crafting.lightning_conversion_chance", conversionPercentage + "%");
        
        int conversionTextX = attractTextX;
        guiGraphics.drawString(Minecraft.getInstance().font, conversionText, conversionTextX, 55, 0x555555, false);
    }
}