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
import com.xiaoyu.more_world_crafting.compat.jei.api.JEIDrawHelper;
import com.xiaoyu.more_world_crafting.compat.jei.api.JEIMultiIconUtils;
import com.xiaoyu.more_world_crafting.compat.jei.api.MultiIconProvider;
import com.xiaoyu.more_world_crafting.recipe.FluidFusionRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import mezz.jei.api.forge.ForgeTypes;

public class FluidFusionCategory implements IRecipeCategory<FluidFusionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MoreWorldCrafting.MODID, "fluid_fusion");
    public static final RecipeType<FluidFusionRecipe> FLUID_FUSION_TYPE = new RecipeType<>(UID, FluidFusionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final JEIDrawHelper drawHelper;
    private final IMultiIconProvider catalystIconProvider;

    private int[][] getItemPositions(int count) {
        int baseX = 9;
        int baseY = 11;
        int spacing = 18;
        
        switch (count) {
            case 1:
                return new int[][]{{baseX + spacing + spacing / 2, baseY + spacing / 2}};
            case 2:
                return new int[][]{
                    {baseX + spacing, baseY + spacing / 2},
                    {baseX + spacing * 2, baseY + spacing / 2}
                };
            case 3:
                return new int[][]{
                    {baseX + spacing + spacing / 2, baseY},
                    {baseX + spacing, baseY + spacing},
                    {baseX + spacing * 2, baseY + spacing}
                };
            case 4:
                return new int[][]{
                    {baseX + spacing, baseY},
                    {baseX + spacing * 2, baseY},
                    {baseX + spacing, baseY + spacing},
                    {baseX + spacing * 2, baseY + spacing}
                };
            case 5:
                return new int[][]{
                    {baseX + spacing, baseY},
                    {baseX + spacing * 2, baseY},
                    {baseX + spacing + spacing / 2, baseY + spacing / 2},
                    {baseX + spacing, baseY + spacing},
                    {baseX + spacing * 2, baseY + spacing}
                };
            case 6:
                return new int[][]{
                    {baseX + spacing / 2, baseY},
                    {baseX + spacing + spacing / 2, baseY},
                    {baseX + spacing * 2 + spacing / 2, baseY},
                    {baseX + spacing / 2, baseY + spacing},
                    {baseX + spacing + spacing / 2, baseY + spacing},
                    {baseX + spacing * 2 + spacing / 2, baseY + spacing}
                };
            case 7:
                return new int[][]{
                    {baseX + spacing / 2, baseY},
                    {baseX + spacing + spacing / 2, baseY},
                    {baseX + spacing * 2 + spacing / 2, baseY},
                    {baseX, baseY + spacing},
                    {baseX + spacing, baseY + spacing},
                    {baseX + spacing * 2, baseY + spacing},
                    {baseX + spacing * 3, baseY + spacing}
                };
            default:
                int[][] positions = new int[Math.min(count, 8)][];
                for (int i = 0; i < Math.min(count, 8); i++) {
                    positions[i] = new int[]{
                        baseX + (i % 4) * spacing,
                        baseY + (i / 4) * spacing
                    };
                }
            return positions;
        }
    }

    public FluidFusionCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(176, 85);
        
        this.icon = JEIMultiIconUtils.createWaterIcon(helper);
        
        this.catalystIconProvider = new MultiIconProvider.Builder(helper)
            .addItemIcon(new ItemStack(Items.WATER_BUCKET))
            .addFluidIcon(new FluidStack(Fluids.WATER, 1000))
            .setAlternating(true)
            .setAlternatingInterval(30)
            .build();
            
        this.drawHelper = new JEIDrawHelper(helper);
    }

    @Override
    public RecipeType<FluidFusionRecipe> getRecipeType() {
        return FLUID_FUSION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.more_world_crafting.fluid_fusion");
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
    public void setRecipe(IRecipeLayoutBuilder builder, FluidFusionRecipe recipe, IFocusGroup focuses) {
        int ingredientCount = recipe.getIngredients().size();
        
        int[][] positions = getItemPositions(ingredientCount);
        for (int i = 0; i < Math.min(ingredientCount, 8); i++) {
            int x = positions[i][0];
            int y = positions[i][1];
            
            net.minecraft.world.item.crafting.Ingredient ingredient = recipe.getIngredients().get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(ingredient.getItems()));
        }

        FluidStack requiredFluidStack = new FluidStack(recipe.getRequiredFluid(), 1000);
        builder.addSlot(RecipeIngredientRole.CATALYST, 92, 20)
            .addIngredient(ForgeTypes.FLUID_STACK, requiredFluidStack)
            .setSlotName("required_fluid");

        builder.addSlot(RecipeIngredientRole.OUTPUT, 142, 20)
            .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(FluidFusionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int ingredientCount = recipe.getIngredients().size();
        
        int[][] positions = getItemPositions(ingredientCount);
        for (int i = 0; i < Math.min(ingredientCount, 8); i++) {
            int x = positions[i][0] - 1;
            int y = positions[i][1] - 1;
            drawHelper.drawSlot(guiGraphics, x, y);
        }
        
        drawHelper.drawSlot(guiGraphics, 91, 19);
        drawHelper.drawArrow(guiGraphics, 115, 19);
        drawHelper.drawSlot(guiGraphics, 141, 19);
        
        double fusionTimeInSeconds = recipe.getFusionTime() / 20.0;
        String timeFormat = (fusionTimeInSeconds == Math.floor(fusionTimeInSeconds)) ? "%.0f" : "%.1f";
        Component timeText = Component.translatable("jei.more_world_crafting.fusion_time", String.format(timeFormat, fusionTimeInSeconds));
        guiGraphics.drawString(Minecraft.getInstance().font, timeText, 8, 61, 0x404040, false);
        
        Component infoText = Component.translatable("jei.more_world_crafting.fluid_fusion_info");
        guiGraphics.drawString(Minecraft.getInstance().font, infoText, 8, 71, 0x606060, false);
    }
}