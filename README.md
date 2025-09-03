[English](#english) | [中文](#中文)

---

## 中文

### 模组简介

**模组为我的世界添加了7种合成类型, 支持使用[KubeJS](#KubeJS配方示例)和[CraftTweaker](#CraftTweaker配方示例)编写。**

### 合成类型

1. **流体融合** - 将多个物品丢入指定流体中融合
2. **流体转换** - 在指定流体中转换单个物品
3. **虚空转换** - 在虚空深度（Y轴-65）进行转换
4. **火焰转换** - 使用火焰类型进行转换
5. **闪电转换** - 通过闪电进行转换
6. **爆炸转换** - 利用爆炸进行转换
7. **粉碎转换** - 使用重力方块砸落进行转换

### [KubeJS](https://kubejs.com/wiki/tutorials)配方示例

在 `kubejs/server_scripts/` 目录下创建脚本文件：

```javascript
// 更多世界合成KubeJS-1.20.1代码示例
ServerEvents.recipes(event => {
    // 流体融合：将两个以上的输入物品丢入流体中融合一段时间后得到输出物品[输入物品超过8个以上不会显示超过后的输入物品]
    event.recipes.more_world_crafting.fluid_fusion({
        ingredients: [
            'minecraft:diamond',
            'minecraft:gold_ingot',
            'minecraft:emerald',
        ], // 输入物品
        result: 'minecraft:netherite_ingot', // 输出物品
        required_fluid: 'minecraft:water', // 指定流体
        fusion_time: 200 // 融合所需时间[tick]
    })

    // 流体转换：将一个以上的输入物品丢入流体中有概率转换成输出物品
    event.recipes.more_world_crafting.fluid_conversion({
        ingredient: 'minecraft:iron_ingot', // 输入物品
        result: 'minecraft:diamond', // 输出物品
        required_fluid: 'minecraft:lava', // 指定流体
        conversion_chance: 0.3 // 转换成功率[百分比]
    })

    // 虚空转换：将一个以上的输入物品丢入虚空[Y轴高度-65]有概率转换成输出物品并出现在转换者的背包内[如果背包满的话则会出现在转换者身上掉落]
    event.recipes.more_world_crafting.void_fusion({
        ingredient: 'minecraft:obsidian', // 输入物品
        result: 'minecraft:crying_obsidian', // 输出物品
        conversion_chance: 0.9 // 转换成功率[百分比]
    })

    // 火焰转换：将一个以上的输入物品丢入火焰中有概率转换成输出物品
    event.recipes.more_world_crafting.fire_fusion({
        ingredient: 'minecraft:coal_block', // 输入物品
        result: 'minecraft:diamond', // 输出物品
        conversion_chance: 0.2, // 转换成功率[百分比]
        fire_type: 'minecraft:fire' // 指定火焰
        // [fire_type: "minecraft:fire" "minecraft:soul_fire" "minecraft:campfire" "minecraft:soul_campfire"]
    })

    // 闪电转换：将一个以上的输入物品丢弃[有概率吸引闪电劈向自身]随后经过闪电劈下以后有概率从而转换成输出物品
    event.recipes.more_world_crafting.lightning_conversion({
        ingredient: 'minecraft:iron_block', // 输入物品
        result: 'minecraft:diamond_block', // 输出物品
        lightning_chance: 0.05, // 吸引闪电几率[百分比]
        conversion_chance: 0.8 // 转换成功率[百分比]
    })

    // 爆炸转换：将一个以上的输入物品丢到即将爆炸的爆炸物[例如tnt]范围内经过爆炸以后有概率从而转换成输出物品
    event.recipes.more_world_crafting.explosion_conversion({
        ingredient: 'minecraft:stone', // 输入物品
        result: 'minecraft:cobblestone', // 输出物品
        conversion_chance: 1.0 // 转换成功率[百分比]
    })

    // 粉碎转换：将一个以上的输入物品丢弃随后指定重力方块从距离物品的指定往上的高度砸落输入物品有概率从而转换成输出物品
    event.recipes.more_world_crafting.crushing_conversion({
        ingredient: 'minecraft:wheat', // 输入物品
        result: 'minecraft:bread', // 输出物品
        gravity_type: 'minecraft:sand', // 指定重力方块
        min_height: 5, // 距离物品砸向的最小高度[格]
        conversion_chance: 0.9 // 转换成功率[百分比]
    })
})
```

### [CraftTweaker](https://docs.blamejared.com/1.20.1)配方示例

在 `scripts/` 目录下创建 `.zs` 文件：

```zenscript
// 更多世界合成CraftTweaker-1.20.1代码示例
import mods.more_world_crafting.MoreWorldCrafting;

// 1. 流体融合[配方示例]

// 水源融合 - 将两个以上的输入物品丢入流体中融合一段时间后得到输出物品[输入物品超过8个以上不会显示超过后的输入物品]
MoreWorldCrafting.fluidFusion.addRecipe("netherite_fusion", // 配方名称
    [<item:minecraft:iron_ingot>, <item:minecraft:gold_ingot>], // 输入物品
    <item:minecraft:diamond>, // 输出物品
    40, // 融合所需时间[tick]
    'minecraft:water'
);

// 2. 流体转换[配方示例]

// 岩浆转换 - 将一个以上的输入物品丢入流体中有概率转换成输出物品
MoreWorldCrafting.fluidConversion.addRecipe("cobblestone_to_obsidian", // 配方名称
    <item:minecraft:cobblestone>, // 输入物品
    <item:minecraft:obsidian>, // 输出物品
    0.1, // 转换成功率[百分比]
    'minecraft:lava'
);

// 3. 虚空转换[配方示例]

// 虚空转换 - 将一个以上的输入物品丢入虚空[Y轴高度-65]有概率转换成输出物品并出现在转换者的背包内[如果背包满的话则会出现在转换者身上掉落]
MoreWorldCrafting.voidFusion.addRecipe("nether_star_to_dragon_egg", // 配方名称
    <item:minecraft:nether_star>, // 输入物品
    <item:minecraft:dragon_egg>, // 输出物品
    0.1 // 转换成功率[百分比]
);

// 4. 火焰转换[配方示例]

// 篝火转换 - 将一个以上的输入物品丢入火焰中有概率转换成输出物品
MoreWorldCrafting.fireFusion.addRecipe("cook_beef", // 配方名称
    <item:minecraft:beef>, // 输入物品
    <item:minecraft:cooked_beef>, // 输出物品
    0.9, // 转换成功率[百分比]
    "minecraft:campfire" // 指定火焰[篝火]
    // [fire_type: "minecraft:fire" "minecraft:soul_fire" "minecraft:campfire" "minecraft:soul_campfire"]
);

// 5. 闪电转换[配方示例]

// 闪电转换 - 将一个以上的输入物品丢弃[有概率吸引闪电劈向自身]随后经过闪电劈下以后有概率从而转换成输出物品
MoreWorldCrafting.lightningConversion.addRecipe("sand_to_glass", // 配方名称
    <item:minecraft:sand>, // 输入物品
    <item:minecraft:glass>, // 输出物品
    1.0, // 转换成功率[百分比]
    0.1  // 吸引闪电几率[百分比]
);

// 6. 爆炸转换[配方示例]

// 爆炸转换 - 将一个以上的输入物品丢到即将爆炸的爆炸物[例如tnt]范围内经过爆炸以后有概率从而转换成输出物品
MoreWorldCrafting.explosionConversion.addRecipe("stone_fragments", // 配方名称
    <item:minecraft:stone>, // 输入物品
    <item:minecraft:gravel>, // 输出物品
    0.8 // 转换成功率[百分比]
);

// 7. 粉碎转换[配方示例]

// 铁砧粉碎 - 将一个以上的输入物品丢弃随后指定重力方块从距离物品的指定往上的高度砸落输入物品有概率从而转换成输出物品
MoreWorldCrafting.crushingConversion.addRecipe("iron_ore_crushing", // 配方名称
    <item:minecraft:iron_ore>, // 输入物品
    <item:minecraft:raw_iron> * 3, // 输出物品 * 数量
    1, // 转换成功率[百分比]
    0, // 距离物品砸向的最小高度[格]
    "minecraft:anvil" // 指定重力方块
);
```

---

## English

### Mod Overview

**World Alchemy Crafting** introduces innovative crafting mechanics that utilize environmental interactions and natural phenomena. This comprehensive modding framework supports both [KubeJS](#kubejs-recipe-examples) and [CraftTweaker](#crafttweaker-recipe-examples) integration for custom recipe creation.

### Crafting Mechanics

1. **Liquid Alchemy** - Combine multiple materials within specific liquids
2. **Elemental Transmutation** - Transform items through elemental liquid exposure
3. **Abyssal Transformation** - Harness the void's power at bedrock depths
4. **Infernal Forging** - Utilize various flame sources for item creation
5. **Storm Crafting** - Channel lightning energy for material conversion
6. **Blast Synthesis** - Employ controlled explosions for item transformation
7. **Impact Smithing** - Use gravity-driven compression techniques

### [KubeJS](https://kubejs.com/wiki/tutorials) Recipe Examples

Create recipe scripts in the `kubejs/server_scripts/` directory:

```javascript
// World Alchemy Crafting KubeJS Examples - Version 1.20.1
ServerEvents.recipes(event => {
    // Liquid Alchemy: Create mystical alloys by combining rare materials in enchanted fluids
    event.recipes.more_world_crafting.fluid_fusion({
        ingredients: [
            'minecraft:nether_star',
            'minecraft:dragon_breath',
            'minecraft:totem_of_undying',
        ], // Mystical components
        result: 'minecraft:enchanted_golden_apple', // Legendary result
        required_fluid: 'minecraft:water', // Pure water medium
        fusion_time: 600 // Extended fusion duration
    })

    // Elemental Transmutation: Transform base metals through molten magic
    event.recipes.more_world_crafting.fluid_conversion({
        ingredient: 'minecraft:copper_ingot', // Base metal
        result: 'minecraft:gold_ingot', // Precious metal
        required_fluid: 'minecraft:lava', // Molten medium
        conversion_chance: 0.15 // Alchemical success rate
    })

    // Void Fusion: Throw one or more input items into void [Y-axis height -65] with a chance to convert to output items and appear in converter's inventory [If inventory is full, items will drop on the converter]
    event.recipes.more_world_crafting.void_fusion({
        ingredient: 'minecraft:obsidian', // Input item
        result: 'minecraft:crying_obsidian', // Output item
        conversion_chance: 0.9 // Conversion success rate [percentage]
    })

    // Fire Fusion: Throw one or more input items into fire with a chance to convert to output items
    event.recipes.more_world_crafting.fire_fusion({
        ingredient: 'minecraft:coal_block', // Input item
        result: 'minecraft:diamond', // Output item
        conversion_chance: 0.2, // Conversion success rate [percentage]
        fire_type: 'minecraft:fire' // Specified fire type
        // [fire_type: "minecraft:fire" "minecraft:soul_fire" "minecraft:campfire" "minecraft:soul_campfire"]
    })

    // Lightning Conversion: Drop one or more input items [with a chance to attract lightning to strike itself] then after lightning strikes, there's a chance to convert to output items
    event.recipes.more_world_crafting.lightning_conversion({
        ingredient: 'minecraft:iron_block', // Input item
        result: 'minecraft:diamond_block', // Output item
        lightning_chance: 0.05, // Lightning attraction chance [percentage]
        conversion_chance: 0.8 // Conversion success rate [percentage]
    })

    // Explosion Conversion: Throw one or more input items into the range of explosives about to explode [e.g., TNT], after explosion there's a chance to convert to output items
    event.recipes.more_world_crafting.explosion_conversion({
        ingredient: 'minecraft:stone', // Input item
        result: 'minecraft:cobblestone', // Output item
        conversion_chance: 1.0 // Conversion success rate [percentage]
    })

    // Crushing Conversion: Drop one or more input items, then specified gravity blocks fall from specified height above the items, with a chance to convert input items to output items
    event.recipes.more_world_crafting.crushing_conversion({
        ingredient: 'minecraft:wheat', // Input item
        result: 'minecraft:bread', // Output item
        gravity_type: 'minecraft:sand', // Specified gravity block
        min_height: 5, // Minimum height above items to fall from [blocks]
        conversion_chance: 0.9 // Conversion success rate [percentage]
    })
})
```

### [CraftTweaker](https://docs.blamejared.com/1.20.1) Recipe Examples

Create `.zs` files in the `scripts/` directory:

```zenscript
// More World Crafting CraftTweaker-1.20.1 Code Examples
import mods.more_world_crafting.MoreWorldCrafting;

// 1. Fluid Fusion [Recipe Example]

// Water Fusion - Throw two or more input items into fluid to fuse for a period of time to get output items [If input items exceed 8, excess items won't be displayed]
MoreWorldCrafting.fluidFusion.addRecipe("netherite_fusion", // Recipe name
    [<item:minecraft:iron_ingot>, <item:minecraft:gold_ingot>], // Input items
    <item:minecraft:diamond>, // Output item
    40, // Fusion time required [ticks]
    'minecraft:water'
);

// 2. Fluid Conversion [Recipe Example]

// Lava Conversion - Throw one or more input items into fluid with a chance to convert to output items
MoreWorldCrafting.fluidConversion.addRecipe("cobblestone_to_obsidian", // Recipe name
    <item:minecraft:cobblestone>, // Input item
    <item:minecraft:obsidian>, // Output item
    0.1, // Conversion success rate [percentage]
    'minecraft:lava'
);

// 3. Void Conversion [Recipe Example]

// Void Conversion - Throw one or more input items into void [Y-axis height -65] with a chance to convert to output items and appear in converter's inventory [If inventory is full, items will drop on the converter]
MoreWorldCrafting.voidFusion.addRecipe("nether_star_to_dragon_egg", // Recipe name
    <item:minecraft:nether_star>, // Input item
    <item:minecraft:dragon_egg>, // Output item
    0.1 // Conversion success rate [percentage]
);

// 4. Fire Conversion [Recipe Example]

// Campfire Conversion - Throw one or more input items into fire with a chance to convert to output items
MoreWorldCrafting.fireFusion.addRecipe("cook_beef", // Recipe name
    <item:minecraft:beef>, // Input item
    <item:minecraft:cooked_beef>, // Output item
    0.9, // Conversion success rate [percentage]
    "minecraft:campfire" // Specified fire type [campfire]
    // [fire_type: "minecraft:fire" "minecraft:soul_fire" "minecraft:campfire" "minecraft:soul_campfire"]
);

// 5. Lightning Conversion [Recipe Example]

// Lightning Conversion - Drop one or more input items [with a chance to attract lightning to strike itself] then after lightning strikes, there's a chance to convert to output items
MoreWorldCrafting.lightningConversion.addRecipe("sand_to_glass", // Recipe name
    <item:minecraft:sand>, // Input item
    <item:minecraft:glass>, // Output item
    1.0, // Conversion success rate [percentage]
    0.1  // Lightning attraction chance [percentage]
);

// 6. Explosion Conversion [Recipe Example]

// Explosion Conversion - Throw one or more input items into the range of explosives about to explode [e.g., TNT], after explosion there's a chance to convert to output items
MoreWorldCrafting.explosionConversion.addRecipe("stone_fragments", // Recipe name
    <item:minecraft:stone>, // Input item
    <item:minecraft:gravel>, // Output item
    0.8 // Conversion success rate [percentage]
);

// 7. Crushing Conversion [Recipe Example]

// Anvil Crushing - Drop one or more input items, then specified gravity blocks fall from specified height above the items, with a chance to convert input items to output items
MoreWorldCrafting.crushingConversion.addRecipe("iron_ore_crushing", // Recipe name
    <item:minecraft:iron_ore>, // Input item
    <item:minecraft:raw_iron> * 3, // Output item * quantity
    1, // Conversion success rate [percentage]
    0, // Minimum height above items to fall from [blocks]
    "minecraft:anvil" // Specified gravity block
);
```