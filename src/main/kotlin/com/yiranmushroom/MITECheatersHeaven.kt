package com.yiranmushroom

import com.yiranmushroom.config.MITECheatersHeavenConfig
import com.yiranmushroom.event.MCHEventListener
import com.yiranmushroom.mixin_helper.NightVision
import com.yiranmushroom.scripting.ScriptingEngine
import fi.dy.masa.malilib.config.ConfigManager
import fi.dy.masa.malilib.event.InitializationHandler
import net.fabricmc.api.ModInitializer
import net.xiaoyu233.fml.ModResourceManager
//import net.xiaoyu233.fml.ModResourceManager
import net.xiaoyu233.fml.reload.event.MITEEvents
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


class MITECheatersHeaven : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        preInit()

        LOGGER.info("Hello MITE Dev world!")

        InitializationHandler.getInstance().registerInitializationHandler(MITECheatersHeavenInitHandler())

        // Add resource pack domain, default "minecraft"
        ModResourceManager.addResourcePackDomain(MOD_ID)

        //Register an event listening object
        MITEEvents.MITE_EVENT_BUS.register(MCHEventListener())
    }

    fun preInit() {
        MITECheatersHeavenConfig.init()

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

    companion object {
        const val MOD_ID: String = "mite-cheaters-heaven"

        // This logger is used to write text to the console and the log file.
        // It is considered best practice to use your mod id as the logger's name.
        // That way, it's clear which mod wrote info, warnings, and errors.
        @JvmStatic
        val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    }
}
