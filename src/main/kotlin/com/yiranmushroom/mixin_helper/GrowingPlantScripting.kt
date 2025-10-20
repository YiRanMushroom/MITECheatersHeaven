package com.yiranmushroom.mixin_helper

import net.minecraft.BlockGrowingPlant
import net.minecraft.BlockPlant

object GrowingPlantScripting {
    @JvmStatic
    var globalGrowingSpeedTransformer : (BlockGrowingPlant.(baseSpeed: Float) -> Float)? = null

    @JvmStatic
    var checkLightLevelTransformer: (BlockGrowingPlant.(Boolean) -> Boolean)? = null

    @JvmStatic
    var chanceOfBlightTransformer: (BlockPlant.(originalChance: Float) -> Float)? = null
}