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
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.jei.api.IMultiIconProvider;
import com.xiaoyu.more_world_crafting.compat.jei.api.JEIBurningAnimation;
import com.xiaoyu.more_world_crafting.compat.jei.api.JEIDrawHelper;
import com.xiaoyu.more_world_crafting.compat.jei.api.MultiIconProvider;
import com.xiaoyu.more_world_crafting.recipe.FireFusionRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FireFusionCategory implements IRecipeCategory<FireFusionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MoreWorldCrafting.MODID, "fire_fusion");
    public static final RecipeType<FireFusionRecipe> FIRE_FUSION_TYPE = new RecipeType<>(UID, FireFusionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final JEIDrawHelper drawHelper;
    private final IMultiIconProvider catalystIconProvider;
    private final IGuiHelper guiHelper;

    public FireFusionCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 60);
        this.icon = JEIBurningAnimation.create(helper, FireFusionRecipe.FireType.NORMAL_FIRE);
        this.catalystIconProvider = new MultiIconProvider.Builder(helper)
            .addItemIcon(new ItemStack(Items.CAMPFIRE))
            .addItemIcon(new ItemStack(Items.SOUL_CAMPFIRE))
            .setAlternating(true)
            .setAlternatingInterval(30)
            .build();
        this.drawHelper = new JEIDrawHelper(helper);
        this.guiHelper = helper;
    }

    @Override
    public RecipeType<FireFusionRecipe> getRecipeType() {
        return FIRE_FUSION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.more_world_crafting.fire_fusion");
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
    public void setRecipe(IRecipeLayoutBuilder builder, FireFusionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 22)
            .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getIngredient().getItems()));

        builder.addSlot(RecipeIngredientRole.CATALYST, 46, 22)
            .setSlotName("fire");

        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 22)
            .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(FireFusionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawHelper.drawSlot(guiGraphics, 9, 21);
        drawHelper.drawSlot(guiGraphics, 115, 21);

        drawHelper.drawArrow(guiGraphics, 75, 24);

        IDrawable fireDrawable = JEIBurningAnimation.create(guiHelper, recipe.getFireType());
        fireDrawable.draw(guiGraphics, 46, 22);

        float chance = recipe.getConversionChance();
        int percentage = Math.round(chance * 100);
        Component chanceText = Component.translatable("jei.more_world_crafting.conversion_chance", percentage + "%");
        
        int centerX = (9 + 45 + 18) / 2;
        int textX = centerX - Minecraft.getInstance().font.width(chanceText) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, chanceText, textX, 45, 0x555555, false);
    }
}