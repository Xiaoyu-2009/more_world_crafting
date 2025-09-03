package com.xiaoyu.more_world_crafting.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.xiaoyu.more_world_crafting.config.ModConfig;
import com.xiaoyu.more_world_crafting.recipe.LightningConversionRecipe;
import com.xiaoyu.more_world_crafting.recipe.ModRecipeTypes;

public class LightningConversionManager {
    private static final Map<ResourceLocation, LightningConversionRecipe> customRecipes = new HashMap<>();
    private static final int CHECK_INTERVAL = 20;

    public static void tick(Level level) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;
        if (level.getGameTime() % CHECK_INTERVAL != 0) return;
        
        if (!ModConfig.LIGHTNING_CONVERSION_ATTRACTS_LIGHTNING.get()) {
            return;
        }
        
        for (var entity : serverLevel.getAllEntities()) {
            if (!(entity instanceof ItemEntity item) || item.getItem().isEmpty()) continue;
            
            LightningConversionRecipe recipe = getRecipe(serverLevel, item.getItem());
            if (recipe != null) {
                if (ModConfig.THUNDERSTORM_DETECTION.get() && !serverLevel.isThundering()) {
                    continue;
                }
                
                boolean shouldCreateLightning = serverLevel.random.nextFloat() < recipe.getLightningChance();
                
                if (shouldCreateLightning) {
                    createLightning(serverLevel, item.position());
                    
                    boolean shouldConvert = serverLevel.random.nextFloat() < recipe.getConversionChance();
                    if (shouldConvert) {
                        if (convertItem(serverLevel, item, recipe)) {
                            item.discard();
                        }
                    }
                }
            }
        }
    }

    private static LightningConversionRecipe getRecipe(ServerLevel level, ItemStack stack) {
        List<LightningConversionRecipe> allRecipes = new ArrayList<>();
        
        List<LightningConversionRecipe> datapackRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.LIGHTNING_CONVERSION.get());
        allRecipes.addAll(datapackRecipes);
        
        allRecipes.addAll(customRecipes.values());
        
        return allRecipes.stream().filter(recipe -> recipe.getIngredient().test(stack)).findFirst().orElse(null);
    }

    private static boolean convertItem(ServerLevel level, ItemEntity item, LightningConversionRecipe recipe) {
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
        if (result.isEmpty()) return false;

        result.setCount(item.getItem().getCount() * result.getCount());

        ItemEntity resultEntity = CraftingAPI.createOutputItemWithJump(level, item.position(), result, item, 0.3);
        
        return level.addFreshEntity(resultEntity);
    }

    private static void createLightning(ServerLevel level, Vec3 position) {
        LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        lightning.moveTo(position.x, position.y, position.z);
        lightning.setVisualOnly(false);
        level.addFreshEntity(lightning);
    }
    
    public static void addCustomRecipe(LightningConversionRecipe recipe) {
        customRecipes.put(recipe.getId(), recipe);
    }
    
    public static void removeCustomRecipe(ResourceLocation recipeId) {
        customRecipes.remove(recipeId);
    }
    
    public static java.util.Collection<LightningConversionRecipe> getCustomRecipes() {
        return customRecipes.values();
    }
}