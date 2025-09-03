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

public class CrushingConversionRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int minHeight;
    private final float conversionChance;
    private final String gravityType;

    public CrushingConversionRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, int minHeight, 
    float conversionChance, String gravityType) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.minHeight = minHeight;
        this.conversionChance = conversionChance;
        this.gravityType = gravityType;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public float getConversionChance() {
        return conversionChance;
    }

    public String getGravityType() {
        return gravityType;
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
        return ModRecipeSerializers.CRUSHING_CONVERSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.CRUSHING_CONVERSION.get();
    }

    public static class Serializer implements RecipeSerializer<CrushingConversionRecipe> {

        @Override
        public CrushingConversionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));

            JsonObject resultObject = GsonHelper.getAsJsonObject(json, "result");
            ItemStack result = new ItemStack(
                GsonHelper.getAsItem(resultObject, "item"),
                GsonHelper.getAsInt(resultObject, "count", 1)
            );

            int minHeight = GsonHelper.getAsInt(json, "min_height", 3);
            float conversionChance = GsonHelper.getAsFloat(json, "conversion_chance", 1.0f);
            
            String gravityType = GsonHelper.getAsString(json, "gravity_type", "minecraft:sand");

            return new CrushingConversionRecipe(recipeId, ingredient, result, minHeight, conversionChance, gravityType);
        }

        @Override
        public @Nullable CrushingConversionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int minHeight = buffer.readVarInt();
            float conversionChance = buffer.readFloat();
            String gravityType = buffer.readUtf();

            return new CrushingConversionRecipe(recipeId, ingredient, result, minHeight, conversionChance, gravityType);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrushingConversionRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeVarInt(recipe.minHeight);
            buffer.writeFloat(recipe.conversionChance);
            buffer.writeUtf(recipe.gravityType);
        }
    }
}