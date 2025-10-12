package com.yiranmushroom.mixin_helper

import net.minecraft.ItemStack

object ItemRockScripting {
    @JvmStatic
    var ExperienceTransform : ((ItemStack, Int) -> Int) ? = null
}