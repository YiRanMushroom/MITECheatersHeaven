package com.yiranmushroom

import com.yiranmushroom.MITECheatersHeaven.Companion.LOGGER
import com.yiranmushroom.config.MITECheatersHeavenConfig
import com.yiranmushroom.mixin_helper.NightVision
import com.yiranmushroom.scripting.ScriptingEngine
import fi.dy.masa.malilib.config.ConfigManager
import fi.dy.masa.malilib.interfaces.IInitializationHandler

class MITECheatersHeavenInitHandler : IInitializationHandler {
    override fun registerModHandlers() {
        val config = MITECheatersHeavenConfig.Instance
        config.load()
        ConfigManager.getInstance().registerConfig(config)
        MITECheatersHeavenConfig.NightVisionToggleHotkey.keybind.setCallback { _,_ ->
            LOGGER.info("Toggling Night Vision")
            NightVision.enabled = !NightVision.enabled
            true
        }

        ScriptingEngine.Init()
    }
}