package com.yiranmushroom

import com.yiranmushroom.config.MITECheatersHeavenConfig
import com.yiranmushroom.config.MITECheatersHeavenConfigScreen
import io.github.prospector.modmenu.api.ConfigScreenFactory
import io.github.prospector.modmenu.api.ModMenuApi

class MITECheatersHeavenModMenu : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            MITECheatersHeavenConfigScreen(parent, MITECheatersHeavenConfig.Instance)
        }
    }
}