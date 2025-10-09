package com.yiranmushroom.config

import fi.dy.masa.malilib.config.interfaces.IConfigHandler
import fi.dy.masa.malilib.gui.screen.DefaultConfigScreen
import net.minecraft.GuiScreen


class MITECheatersHeavenConfigScreen : DefaultConfigScreen {
    constructor(parentScreen: GuiScreen?, configInstance: IConfigHandler) : super(parentScreen, configInstance)
}