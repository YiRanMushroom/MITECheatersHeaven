package com.yiranmushroom.mixin_helper

import net.minecraft.CraftingResult
import net.minecraft.EnumQuality

object CraftingResultScripting {
    @JvmStatic
    var getQualityAdjustedDifficultyModify : CraftingResult.(EnumQuality?, Float) -> Float =
        { _: EnumQuality?, original: Float -> original }
}