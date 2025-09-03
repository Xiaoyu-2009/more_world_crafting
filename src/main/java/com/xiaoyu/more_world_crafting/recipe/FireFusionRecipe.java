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

public class FireFusionRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float conversionChance;
    private final FireType fireType;

    public enum FireType {
        NORMAL_FIRE("minecraft:fire"),
        SOUL_FIRE("minecraft:soul_fire"),
        CAMPFIRE("minecraft:campfire"),
        SOUL_CAMPFIRE("minecraft:soul_campfire");

        private final String itemId;

        FireType(String itemId) {
            this.itemId = itemId;
        }

        public String getItemId() {
            return itemId;
        }

        public static FireType fromItemId(String itemId) {
            for (FireType type : values()) {
                if (type.itemId.equals(itemId)) {
                    return type;
                }
            }
            return NORMAL_FIRE;
        }
    }

    public FireFusionRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, 
    float conversionChance, FireType fireType) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.conversionChance = conversionChance;
        this.fireType = fireType;
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

    public FireType getFireType() {
        return fireType;
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
        return ModRecipeSerializers.FIRE_FUSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.FIRE_FUSION.get();
    }

    public static class Serializer implements RecipeSerializer<FireFusionRecipe> {

        @Override
        public FireFusionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));

            JsonObject resultObject = GsonHelper.getAsJsonObject(json, "result");
            ItemStack result = new ItemStack(
                GsonHelper.getAsItem(resultObject, "item"),
                GsonHelper.getAsInt(resultObject, "count", 1)
            );

            float conversionChance = GsonHelper.getAsFloat(json, "conversion_chance", 1.0f);
            
            String fireTypeStr = GsonHelper.getAsString(json, "fire_type", "minecraft:fire");
            FireType fireType = FireType.fromItemId(fireTypeStr);

            return new FireFusionRecipe(recipeId, ingredient, result, conversionChance, fireType);
        }

        @Override
        public @Nullable FireFusionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float conversionChance = buffer.readFloat();
            FireType fireType = FireType.values()[buffer.readInt()];

            return new FireFusionRecipe(recipeId, ingredient, result, conversionChance, fireType);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FireFusionRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.conversionChance);
            buffer.writeInt(recipe.fireType.ordinal());
        }
    }
}