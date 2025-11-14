package com.yiranmushroom.ric_handlers

import moddedmite.rustedironcore.api.event.Handlers

object RICRegister {
    fun registerRICHandlers() {
        Handlers.GravelDrop.register(ITHGravelDropHandler())
        Handlers.PlayerAttribute.register(ITHPlayerAttributeHandler())
    }
}