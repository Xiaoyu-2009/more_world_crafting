package com.xiaoyu.more_world_crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class FluidFusionRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int fusionTime;
    private final Fluid requiredFluid;

    public FluidFusionRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, 
    int fusionTime, Fluid requiredFluid) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.fusionTime = fusionTime;
        this.requiredFluid = requiredFluid;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getFusionTime() {
        return fusionTime;
    }

    public Fluid getRequiredFluid() {
        return requiredFluid;
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
        return ModRecipeSerializers.FLUID_FUSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.FLUID_FUSION.get();
    }

    public static class Serializer implements RecipeSerializer<FluidFusionRecipe> {

        @Override
        public FluidFusionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            JsonArray ingredientsArray = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientsArray.size(), Ingredient.EMPTY);
            
            for (int i = 0; i < ingredientsArray.size(); i++) {
                ingredients.set(i, Ingredient.fromJson(ingredientsArray.get(i)));
            }

            JsonObject resultObject = GsonHelper.getAsJsonObject(json, "result");
            ItemStack result = new ItemStack(
                GsonHelper.getAsItem(resultObject, "item"),
                GsonHelper.getAsInt(resultObject, "count", 1)
            );

            int fusionTime = GsonHelper.getAsInt(json, "fusion_time", 60);
            
            String fluidId = GsonHelper.getAsString(json, "required_fluid");
            Fluid requiredFluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidId));
            if (requiredFluid == null) {
                throw new IllegalArgumentException("Unknown fluid: " + fluidId);
            }

            return new FluidFusionRecipe(recipeId, ingredients, result, fusionTime, requiredFluid);
        }

        @Override
        public @Nullable FluidFusionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            
            for (int i = 0; i < ingredientCount; i++) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack result = buffer.readItem();
            int fusionTime = buffer.readVarInt();
            ResourceLocation fluidId = buffer.readResourceLocation();
            Fluid requiredFluid = ForgeRegistries.FLUIDS.getValue(fluidId);

            return new FluidFusionRecipe(recipeId, ingredients, result, fusionTime, requiredFluid);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FluidFusionRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            
            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
            buffer.writeVarInt(recipe.fusionTime);
            buffer.writeResourceLocation(ForgeRegistries.FLUIDS.getKey(recipe.requiredFluid));
        }
    }
}