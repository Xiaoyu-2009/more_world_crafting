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
import com.xiaoyu.more_world_crafting.recipe.ExplosionConversionRecipe;

public class ExplosionConversionCategory implements IRecipeCategory<ExplosionConversionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MoreWorldCrafting.MODID, "explosion_conversion");
    public static final RecipeType<ExplosionConversionRecipe> EXPLOSION_CONVERSION_TYPE = new RecipeType<>(UID, ExplosionConversionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final JEIDrawHelper drawHelper;

    public ExplosionConversionCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, Items.TNT.getDefaultInstance());
        this.drawHelper = new JEIDrawHelper(helper);
    }

    @Override
    public RecipeType<ExplosionConversionRecipe> getRecipeType() {
        return EXPLOSION_CONVERSION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.more_world_crafting.explosion_conversion");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ExplosionConversionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 22)
            .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getIngredient().getItems()));

        builder.addSlot(RecipeIngredientRole.CATALYST, 46, 22)
            .setSlotName("explosion");
            
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 22)
            .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(ExplosionConversionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawHelper.drawSlot(guiGraphics, 9, 21);
        drawHelper.drawSlot(guiGraphics, 115, 21);
        
        ItemStack tntStack = new ItemStack(Items.TNT);
        guiGraphics.renderItem(tntStack, 46, 22);
        
        drawHelper.drawArrow(guiGraphics, 75, 24);
        
        float chance = recipe.getConversionChance();
        int percentage = Math.round(chance * 100);
        Component chanceText = Component.translatable("jei.more_world_crafting.conversion_chance", percentage + "%");
        
        int centerX = (9 + 45 + 18) / 2;
        int textX = centerX - Minecraft.getInstance().font.width(chanceText) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, chanceText, textX, 45, 0x555555, false);
    }
}