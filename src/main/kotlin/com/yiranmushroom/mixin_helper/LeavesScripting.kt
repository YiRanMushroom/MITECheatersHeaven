package com.yiranmushroom.mixin_helper

import net.minecraft.BlockBreakInfo
import net.minecraft.BlockLeaves

object LeavesScripting {
    @JvmStatic
    var redirectedSaplingDropFunction: (BlockLeaves.(
        BlockBreakInfo, Int, Int, Int, Float, ((BlockLeaves, BlockBreakInfo, Int, Int, Int, Float) -> Int)
    ) -> Int)? = null
}