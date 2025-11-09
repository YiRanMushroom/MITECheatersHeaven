package com.yiranmushroom.mixin_helper

object ChainingDropPositionLockHelper {
    @JvmField
    var isPositionLocked: Boolean = false

    @JvmField
    var lockedX: Int = 0

    @JvmField
    var lockedY: Int = 0

    @JvmField
    var lockedZ: Int = 0
}