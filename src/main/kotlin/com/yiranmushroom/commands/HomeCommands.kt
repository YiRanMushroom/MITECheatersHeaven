package com.yiranmushroom.commands

import com.yiranmushroom.MITECheatersHeaven.Companion.LOGGER
import net.minecraft.*

class HomeCommand : CommandBase() {
    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandName(): String {
        return "home"
    }

    override fun getCommandUsage(iCommandSender: ICommandSender): String {
        return "/home [homeName(nullable)]"
    }

    override fun processCommand(
        iCommandSender: ICommandSender?,
        strings: Array<out String?>?
    ) {
        if (iCommandSender !is EntityPlayer) {
            return
        }

        if (strings == null) {
            return
        }

        if (iCommandSender is IHomeCommandContext) {
            var homeName: String? = null
            // If no home name is provided, teleport to default home "@home"
            val coordinate: Triple<Double, Double, Double>?
            if (strings.isEmpty()) {
                coordinate = iCommandSender.getHomeCoordinates("@home")
            } else {
                if (strings.size > 1) {
                    iCommandSender.sendChatToPlayer(
                        ChatMessageComponent
                            .createFromText("Usage: /home [homeName(nullable)]")
                            .setColor(EnumChatFormatting.RED)
                    )
                    return
                }

                coordinate = iCommandSender.getHomeCoordinates(strings[0]!!)
                homeName = strings[0]
            }

            if (coordinate == null) {
                iCommandSender.sendChatToPlayer(
                    ChatMessageComponent
                        .createFromText(
                            if (strings.isEmpty()) {
                                "Default home '@home' not set."
                            } else {
                                "Home '$homeName' not set."
                            }
                        )
                        .setColor(EnumChatFormatting.RED)
                )

                return
            } else {
                iCommandSender.setPositionAndUpdate(
                    coordinate.first,
                    coordinate.second,
                    coordinate.third
                )

                iCommandSender.sendChatToPlayer(
                    ChatMessageComponent
                        .createFromText(
                            if (strings.isEmpty()) {
                                "Teleported to default home."
                            } else {
                                "Teleported to home '$homeName'."
                            }
                        )
                        .setColor(EnumChatFormatting.GREEN)
                )
            }
        } else {
            LOGGER.error("Logic error: EntityPlayer is not IHomeCommandContext")
        }
    }

    override fun addTabCompletionOptions(
        commandSender: ICommandSender,
        commandStrings: Array<String>
    ): List<String> {
        return if (commandSender !is EntityPlayer) {
            emptyList()
        } else {
            if (commandSender is IHomeCommandContext) {
                commandSender.getHomeNames() - listOf("@home")
            } else {
                emptyList()
            }
        }
    }
}

class SetHomeCommand : CommandBase() {
    override fun getCommandName(): String {
        return "sethome"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(iCommandSender: ICommandSender): String {
        return "/sethome [homeName(nullable)]"
    }

    override fun processCommand(
        iCommandSender: ICommandSender?,
        strings: Array<out String?>?
    ) {
        if (iCommandSender !is EntityPlayer) {
            return
        }

        if (strings == null) {
            return
        }

        if (iCommandSender is IHomeCommandContext) {
            val homeName: String = if (strings.isEmpty()) {
                "@home"
            } else {
                if (strings.size > 1) {
                    iCommandSender.sendChatToPlayer(
                        ChatMessageComponent
                            .createFromText("Usage: /sethome [homeName(nullable)]")
                            .setColor(EnumChatFormatting.RED)
                    )
                    return
                }

                strings[0]!!
            }

            val coordinates = Triple(
                iCommandSender.posX,
                iCommandSender.posY,
                iCommandSender.posZ
            )

            iCommandSender.setHomeCoordinates(homeName, coordinates)

            iCommandSender.sendChatToPlayer(
                ChatMessageComponent
                    .createFromText(
                        if (strings.isEmpty()) {
                            "Default home set."
                        } else {
                            "Home '$homeName' set."
                        }
                    )
                    .setColor(EnumChatFormatting.GREEN)
            )
        } else {
            LOGGER.error("Logic error: EntityPlayer is not IHomeCommandContext")
        }
    }
}

class DeleteHomeCommand : CommandBase() {
    override fun getCommandName(): String {
        return "delhome"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(iCommandSender: ICommandSender): String {
        return "/delhome [homeName]"
    }

    override fun processCommand(
        iCommandSender: ICommandSender?,
        strings: Array<out String?>?
    ) {
        if (iCommandSender !is EntityPlayer) {
            return
        }

        if (strings == null) {
            iCommandSender.sendChatToPlayer(
                ChatMessageComponent
                    .createFromText("Usage: /delhome [homeName]")
                    .setColor(EnumChatFormatting.RED)
            )
            return
        }

        if (iCommandSender is IHomeCommandContext) {
            val homeName = strings.getOrNull(0) ?: "@home"

            val success = iCommandSender.deleteHome(homeName)

            if (success) {
                iCommandSender.sendChatToPlayer(
                    ChatMessageComponent
                        .createFromText(
                            if (strings.isEmpty()) {
                                "Default home deleted."
                            } else {
                                "Home '$homeName' deleted."
                            }
                        )
                        .setColor(EnumChatFormatting.GREEN)
                )
            } else {
                iCommandSender.sendChatToPlayer(
                    ChatMessageComponent
                        .createFromText(
                            if (strings.isEmpty()) {
                                "Default home not set."
                            } else {
                                "Home '$homeName' not set."
                            }
                        )
                        .setColor(EnumChatFormatting.RED)
                )
            }
        } else {
            LOGGER.error("Logic error: EntityPlayer is not IHomeCommandContext")
        }
    }
}