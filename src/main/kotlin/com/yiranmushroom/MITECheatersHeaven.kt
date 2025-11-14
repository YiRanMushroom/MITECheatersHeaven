package com.yiranmushroom

//import net.xiaoyu233.fml.ModResourceManager
import com.yiranmushroom.config.MITECheatersHeavenConfig
import com.yiranmushroom.event.MCHEventListener
import com.yiranmushroom.mixin_helper.EntityClientPlayerFlySpeedMixinHelper
import com.yiranmushroom.mixin_helper.NightVision
import com.yiranmushroom.network.C2S.C2SRequestOpenTrashCanPacket
import com.yiranmushroom.ric_handlers.RICRegister
import com.yiranmushroom.scripting.ScriptingEngine
import fi.dy.masa.malilib.config.ConfigManager
import fi.dy.masa.malilib.event.InitializationHandler
import moddedmite.rustedironcore.network.Network
import net.fabricmc.api.ModInitializer
import net.minecraft.ChatMessageComponent
import net.minecraft.EnumChatFormatting
import net.minecraft.Minecraft
import net.xiaoyu233.fml.ModResourceManager
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

        RICRegister.registerRICHandlers()
    }

    fun preInit() {
        MITECheatersHeavenConfig.init()

        val config = MITECheatersHeavenConfig.Instance
        config.load()
        ConfigManager.getInstance().registerConfig(config)
        MITECheatersHeavenConfig.NightVisionToggleHotkey.keybind.setCallback { _, _ ->
            LOGGER.info("Toggling Night Vision")
            NightVision.enabled = !NightVision.enabled
            true
        }

        MITECheatersHeavenConfig.OpenTrashCanHotkey.keybind.setCallback { _, _ ->
            Network.sendToServer(C2SRequestOpenTrashCanPacket())
            true
        }

        MITECheatersHeavenConfig.ToggleFlySpeedHotkey.keybind.setCallback { _, _ ->
            EntityClientPlayerFlySpeedMixinHelper.roundRobinSpeed()
            // send chat message to inform the player about the new fly speed
            val clientPlayer = Minecraft.getClientPlayer() ?: return@setCallback true
            val multiplier = EntityClientPlayerFlySpeedMixinHelper.modifyFlySpeed(1f)
            clientPlayer.sendChatToPlayer(
                ChatMessageComponent()
                    .addText("Fly Speed Multiplier: ")
                    .addText(multiplier.toString()).setColor(EnumChatFormatting.GREEN)
            )
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
