package com.yiranmushroom.config

import com.yiranmushroom.MITECheatersHeaven
import fi.dy.masa.malilib.config.SimpleConfigs
import fi.dy.masa.malilib.config.options.ConfigBase
import fi.dy.masa.malilib.config.options.ConfigHotkey
import net.minecraft.GuiScreen
import org.lwjgl.input.Keyboard

class MITECheatersHeavenConfig : SimpleConfigs {
    constructor(name: String, hotkeys: List<ConfigHotkey>, values: List<ConfigBase<*>>) : super(name, hotkeys, values)



    companion object {
        @JvmStatic
        lateinit var Instance: MITECheatersHeavenConfig

        @JvmStatic
        var NightVisionToggleHotkey = ConfigHotkey("NightVisionToggleHotkey", Keyboard.KEY_G,
            "This is the hotkey to toggle Night Vision on/off")

        fun init() {
            val hotkeys = listOf(NightVisionToggleHotkey)

            val values = listOf<ConfigBase<*>>(
                // Add other config options here
            )

            Instance = MITECheatersHeavenConfig("MITE Cheater's Heaven", hotkeys, values)
        }
    }

    override fun getConfigScreen(parentScreen: GuiScreen): GuiScreen {
        return MITECheatersHeavenConfigScreen(parentScreen, this)
    }
}