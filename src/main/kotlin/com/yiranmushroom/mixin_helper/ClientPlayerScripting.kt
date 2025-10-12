package com.yiranmushroom.mixin_helper

import net.minecraft.ClientPlayer
import net.minecraft.Container

object ClientPlayerScripting {
    @JvmStatic
    var craftingPeriodTransform: (ClientPlayer.(Container?, Int) -> Int)? = null
}