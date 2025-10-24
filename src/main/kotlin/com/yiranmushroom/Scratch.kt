package com.yiranmushroom

import com.yiranmushroom.MITECheatersHeaven.Companion.LOGGER
import com.yiranmushroom.enchantments.Enchantments
import kotlin.math.min

fun executeScratch() {
    Enchantments.anvilEnchantmentLevelTransformHandler.register { id, lvl ->
        if (id == 85) {
//            LOGGER.info("Speed enchantment level is capped, original $lvl, now ${min(10, lvl)}")
            min(10, lvl)
        } else if (id == 88) {
            min(9, lvl)
        } else lvl
    }
}

