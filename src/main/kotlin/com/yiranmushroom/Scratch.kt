package com.yiranmushroom

import com.yiranmushroom.scripting.Events
import net.minecraft.Item
import net.minecraft.ItemStack
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent

fun registerRecipeForAncientMetalAndPaper(event: RecipeRegistryEvent) {
    event.registerShapelessRecipe(
        ItemStack(Item.ingotAncientMetal),
        true,
        Item.mithrilNugget,
        Item.goldNugget,
        Item.silverNugget,
        Item.silverNugget,
        Item.ironNugget,
        Item.ironNugget,
        Item.copperNugget,
        Item.copperNugget,
        Item.copperNugget,
    )

    event.registerShapelessRecipe(
        ItemStack(Item.paper, 3),
        true,
        Item.stick,
        Item.stick,
        Item.stick
    )

    Events.onRecipeRegister("Paper and ancient metal recipe register") {
        event ->
        registerRecipeForAncientMetalAndPaper(event)
    }
}