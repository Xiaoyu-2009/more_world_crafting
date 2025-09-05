package com.xiaoyu.more_world_crafting.recipe;

import javax.annotation.Nullable;

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

public class LightningConversionRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float lightningChance;
    private final float conversionChance;
    private final String weatherId;

    public LightningConversionRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, 
    float lightningChance, float conversionChance, String weatherId) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.lightningChance = lightningChance;
        this.conversionChance = conversionChance;
        this.weatherId = weatherId != null ? weatherId : "";
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public float getLightningChance() {
        return lightningChance;
    }

    public float getConversionChance() {
        return conversionChance;
    }

    public String getWeatherId() {
        return weatherId;
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
        return ModRecipeSerializers.LIGHTNING_CONVERSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.LIGHTNING_CONVERSION.get();
    }

    public static class Serializer implements RecipeSerializer<LightningConversionRecipe> {

        @Override
        public LightningConversionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));

            JsonObject resultObject = GsonHelper.getAsJsonObject(json, "result");
            ItemStack result = new ItemStack(
                GsonHelper.getAsItem(resultObject, "item"),
                GsonHelper.getAsInt(resultObject, "count", 1)
            );

            float lightningChance = GsonHelper.getAsFloat(json, "lightning_chance", 0.1f);
            float conversionChance = GsonHelper.getAsFloat(json, "conversion_chance", 1.0f);
            String weatherId = GsonHelper.getAsString(json, "weather_id", "");

            return new LightningConversionRecipe(recipeId, ingredient, result, lightningChance, conversionChance, weatherId);
        }

        @Override
        public @Nullable LightningConversionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float lightningChance = buffer.readFloat();
            float conversionChance = buffer.readFloat();
            String weatherId = buffer.readUtf();

            return new LightningConversionRecipe(recipeId, ingredient, result, lightningChance, conversionChance, weatherId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, LightningConversionRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.lightningChance);
            buffer.writeFloat(recipe.conversionChance);
            buffer.writeUtf(recipe.weatherId);
        }
    }
}