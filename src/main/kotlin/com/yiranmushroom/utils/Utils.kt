package com.yiranmushroom.utils

import net.xiaoyu233.fml.FishModLoader

object Utils {
    private var IsITELoadedCache : Boolean? = null

    @JvmStatic
    fun isITELoaded() : Boolean {
        return IsITELoadedCache ?: run {
            val isLoaded = FishModLoader.getModContainer("mite_ite") != null
            IsITELoadedCache = isLoaded
            isLoaded
        }
    }
}