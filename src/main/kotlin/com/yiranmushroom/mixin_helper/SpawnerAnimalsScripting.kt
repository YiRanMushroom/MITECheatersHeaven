package com.yiranmushroom.mixin_helper

import net.minecraft.EnumCreatureType
import net.minecraft.World

object SpawnerAnimalsScripting {
    @JvmStatic
    var animalSpawnTickInterval: Int = 80

    @JvmStatic
    var animalSpawnMultiplier: Double = 1.0

    @JvmStatic
    var onTrySpawnPeacefulMob: (World, EnumCreatureType) -> Unit = { _, _ -> }
}