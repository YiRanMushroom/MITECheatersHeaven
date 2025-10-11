package com.yiranmushroom.mixin_helper

import net.minecraft.BiomeDecorator


object BiomeDecoratorScripting {
    @JvmStatic
    var customDecorationCallback: (BiomeDecorator.() -> Unit)? = null
}