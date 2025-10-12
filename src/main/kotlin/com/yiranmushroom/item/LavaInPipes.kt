package com.yiranmushroom.item

import net.minecraft.CreativeTabs
import net.minecraft.Item
import net.minecraft.ItemStack
import net.minecraft.Material


class LavaInPipes : Item {
    constructor(par1: Int, par2Material: Material) : super(par1, Material.lava.setDissolvesInWater(), "lava_in_pipes") {
        this.setMaxStackSize(1)
        this.creativeTab = CreativeTabs.tabMisc
    }

    override fun getBurnTime(item_stack: ItemStack?): Int {
        return 3200
    }

    override fun getHeatLevel(item_stack: ItemStack?): Int {
        return LavaInPipesHeatLevel
    }

    companion object {
        @JvmStatic
        var LavaInPipesHeatLevel: Int = 3
    }
}