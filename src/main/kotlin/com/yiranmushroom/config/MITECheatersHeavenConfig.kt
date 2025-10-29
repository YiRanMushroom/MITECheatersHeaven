package com.yiranmushroom.config

import com.yiranmushroom.utils.GetAllStaticMembersFromClassDerivedFrom
import fi.dy.masa.malilib.config.SimpleConfigs
import fi.dy.masa.malilib.config.options.ConfigBase
import fi.dy.masa.malilib.config.options.ConfigBoolean
import fi.dy.masa.malilib.config.options.ConfigHotkey
import net.minecraft.GuiScreen
import org.lwjgl.input.Keyboard

class MITECheatersHeavenConfig : SimpleConfigs {
    constructor(name: String, hotkeys: List<ConfigHotkey>, values: List<ConfigBase<*>>) : super(name, hotkeys, values)

    companion object {
        @JvmStatic
        lateinit var Instance: MITECheatersHeavenConfig

        @JvmStatic
        var NightVisionToggleHotkey = ConfigHotkey(
            "NightVisionToggleHotkey", Keyboard.KEY_G,
            "This is the hotkey to toggle Night Vision on/off"
        )

        @JvmStatic
        var OpenTrashCanHotKey = ConfigHotkey(
            "OpenTrashCanHotKey", Keyboard.KEY_H,
            "This is the hotkey to open Trash Can GUI"
        )

        @JvmStatic
        var EnableDevModePrivilegeConfig = ConfigBoolean(
            "EnableDevModePrivilegeConfig",
            false, "Enable Dev Mode Privilege"
        )

        @JvmStatic
        var TurnOffRustyIronCoreStatementConfig = ConfigBoolean(
            "TurnOffRustyIronCoreStatementConfig",
            true, "Turn off Rusty Iron Core statement when creating a new world"
        )

        @JvmStatic
        var EnableDevModePrivilegeValue: Boolean
            get() = EnableDevModePrivilegeConfig.booleanValue
            set(value) {
                EnableDevModePrivilegeConfig.booleanValue = value
            }

        fun init() {
            val hotkeys = GetAllStaticMembersFromClassDerivedFrom<MITECheatersHeavenConfig, ConfigHotkey>()

            val values =
                GetAllStaticMembersFromClassDerivedFrom<MITECheatersHeavenConfig, ConfigBase<*>>() - hotkeys.toSet()

            Instance = MITECheatersHeavenConfig("MITE Cheater's Heaven", hotkeys, values)
        }
    }

    override fun getConfigScreen(parentScreen: GuiScreen): GuiScreen {
        return MITECheatersHeavenConfigScreen(parentScreen, this)
    }
}