package com.yiranmushroom.mixin_helper

import net.minecraft.ItemStack
import java.util.*

object EnchantmentScripting {
    @JvmStatic
    var transformEnchantmentLevel: (Random, ItemStack, Int) -> Int =
    { _, _, original -> original }
}