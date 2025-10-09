package com.yiranmushroom

import com.yiranmushroom.event.FishEventListen
import net.fabricmc.api.ModInitializer
import net.xiaoyu233.fml.ModResourceManager
import net.xiaoyu233.fml.reload.event.MITEEvents
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


class MITECheatersHeaven : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello MITE Dev world!")

        // Add resource pack domain, default "minecraft"
        ModResourceManager.addResourcePackDomain(MOD_ID)

        //Register an event listening object
        MITEEvents.MITE_EVENT_BUS.register(FishEventListen())
    }

    companion object {
        const val MOD_ID: String = "mite-cheaters-heaven"

        // This logger is used to write text to the console and the log file.
        // It is considered best practice to use your mod id as the logger's name.
        // That way, it's clear which mod wrote info, warnings, and errors.
        val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    }
}
