package com.xiaoyu.more_world_crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class VoidFusionRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float conversionChance;

    public VoidFusionRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, float conversionChance) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.conversionChance = conversionChance;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public float getConversionChance() {
        return conversionChance;
    }

    public float getSuccessChance() {
        return conversionChance;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.VOID_FUSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.VOID_FUSION.get();
    }

    public static class Serializer implements RecipeSerializer<VoidFusionRecipe> {

        @Override
        public VoidFusionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));

            JsonObject resultObject = GsonHelper.getAsJsonObject(json, "result");
            ItemStack result = new ItemStack(
                GsonHelper.getAsItem(resultObject, "item"),
                GsonHelper.getAsInt(resultObject, "count", 1)
            );

            float conversionChance = GsonHelper.getAsFloat(json, "conversion_chance", 1.0f);

            return new VoidFusionRecipe(recipeId, ingredient, result, conversionChance);
        }

        @Override
        public @Nullable VoidFusionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float conversionChance = buffer.readFloat();

            return new VoidFusionRecipe(recipeId, ingredient, result, conversionChance);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, VoidFusionRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.conversionChance);
        }
    }
}