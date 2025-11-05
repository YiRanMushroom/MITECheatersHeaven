package com.yiranmushroom.commands

import com.yiranmushroom.network.S2C.S2CFlySpeedModifyPacket
import moddedmite.rustedironcore.network.Network
import net.minecraft.*

class FlySpeedCommand : CommandBase() {
    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandName(): String {
        return "flyspeed"
    }

    override fun getCommandUsage(iCommandSender: ICommandSender): String {
        return "/flyspeed set_index <index> or /flyspeed set_modifier <modifier>"
    }

    override fun addTabCompletionOptions(
        par1ICommandSender: ICommandSender,
        par2ArrayOfStr: Array<String>
    ): List<String> {
        // if par2ArrayOfStr length is 0, suggest "set_index" and "set_modifier"
        if (par2ArrayOfStr.isEmpty()) {
            return listOf("set_index", "set_modifier")
        } else {
            if (par2ArrayOfStr.size != 1) {
                return emptyList()
            }

            return getListOfStringsMatchingLastWord(
                par2ArrayOfStr,
                "set_index", "set_modifier"
            ) as List<String>

        }
    }

    override fun processCommand(
        iCommandSender: ICommandSender,
        strings: Array<String>
    ) {
        if (iCommandSender !is ServerPlayer) {
            throw IllegalArgumentException("Command can only be executed by a player!")
        } else {

            if (strings.size != 2) {
                iCommandSender.sendChatToPlayer(
                    ChatMessageComponent().addText("Invalid number of arguments!").setColor(
                        EnumChatFormatting.RED
                    )
                )
            } else {
                when (strings[0]) {
                    "set_index" -> {
                        try {
                            val index = strings[1].toInt()
                            Network.sendToClient(
                                iCommandSender,
                                S2CFlySpeedModifyPacket(true, index, 0.0f)
                            )
                        } catch (e: NumberFormatException) {
                            iCommandSender.sendChatToPlayer(
                                ChatMessageComponent().addText("Index must be an integer!").setColor(
                                    EnumChatFormatting.RED
                                )
                            )
                            return
                        }
                        iCommandSender.sendChatToPlayer(
                            ChatMessageComponent().addText("Set index command executed!").setColor(
                                EnumChatFormatting.GREEN
                            )
                        )
                    }

                    "set_modifier" -> {
                        try {
                            val modifier = strings[1].toFloat()
                            Network.sendToClient(
                                iCommandSender,
                                S2CFlySpeedModifyPacket(false, 0, modifier)
                            )
                        } catch (e: NumberFormatException) {
                            iCommandSender.sendChatToPlayer(
                                ChatMessageComponent().addText("Modifier must be a float!").setColor(
                                    EnumChatFormatting.RED
                                )
                            )
                            return
                        }
                        iCommandSender.sendChatToPlayer(
                            ChatMessageComponent().addText("Set modifier command executed!").setColor(
                                EnumChatFormatting.GREEN
                            )
                        )
                    }

                    else -> {
                        iCommandSender.sendChatToPlayer(
                            ChatMessageComponent().addText("Unknown subcommand!").setColor(
                                EnumChatFormatting.RED
                            )
                        )
                    }
                }
            }
        }
    }
}