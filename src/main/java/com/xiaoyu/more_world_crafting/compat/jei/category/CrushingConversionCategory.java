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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.jei.api.JEIDrawHelper;
import com.xiaoyu.more_world_crafting.recipe.CrushingConversionRecipe;

public class CrushingConversionCategory implements IRecipeCategory<CrushingConversionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MoreWorldCrafting.MODID, "crushing_conversion");
    public static final RecipeType<CrushingConversionRecipe> CRUSHING_CONVERSION_TYPE = new RecipeType<>(UID, CrushingConversionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final JEIDrawHelper drawHelper;
    private final IGuiHelper guiHelper;

    public CrushingConversionCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 70);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.ANVIL));
        this.drawHelper = new JEIDrawHelper(helper);
        this.guiHelper = helper;
    }

    @Override
    public RecipeType<CrushingConversionRecipe> getRecipeType() {
        return CRUSHING_CONVERSION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.more_world_crafting.crushing_conversion");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrushingConversionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 15)
            .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getIngredient().getItems()));

        ItemStack gravityBlockItem = getGravityBlockItem(recipe.getGravityType());
        builder.addSlot(RecipeIngredientRole.CATALYST, 46, 15)
            .addItemStack(gravityBlockItem)
            .setSlotName("gravity_block");

        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 15)
            .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(CrushingConversionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawHelper.drawSlot(guiGraphics, 9, 14);
        drawHelper.drawSlot(guiGraphics, 45, 14);
        drawHelper.drawSlot(guiGraphics, 115, 14);

        drawHelper.drawArrow(guiGraphics, 75, 17);

        Component downText = Component.literal("↓");
        int downTextX = 55 - Minecraft.getInstance().font.width(downText) / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, downText, downTextX, 35, 0x666666, false);

        int minHeight = recipe.getMinHeight();
        Component heightText = Component.translatable("jei.more_world_crafting.min_height", minHeight);
        int heightTextX = 8;
        guiGraphics.drawString(Minecraft.getInstance().font, heightText, heightTextX, 48, 0x404040, false);

        float conversionChance = recipe.getConversionChance();
        int conversionPercentage = Math.round(conversionChance * 100);
        Component chanceText = Component.translatable("jei.more_world_crafting.conversion_chance", conversionPercentage + "%");
        int chanceTextX = 8;
        guiGraphics.drawString(Minecraft.getInstance().font, chanceText, chanceTextX, 58, 0x404040, false);
    }

    private ItemStack getGravityBlockItem(String gravityType) {
        try {
            ResourceLocation itemId = new ResourceLocation(gravityType);
            Item item = BuiltInRegistries.ITEM.get(itemId);
            if (item != null && item != Items.AIR) {
                return new ItemStack(item);
            }
        } catch (Exception e) {
        }
        return new ItemStack(Items.SAND);
    }
}