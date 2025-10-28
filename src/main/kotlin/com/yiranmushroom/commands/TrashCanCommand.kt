package com.yiranmushroom.commands

import com.yiranmushroom.container.IOpenTrashCan
import net.minecraft.CommandBase
import net.minecraft.ICommandSender

class TrashCanCommand : CommandBase() {
    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandName(): String {
        return "trashcan"
    }

    override fun getCommandUsage(iCommandSender: ICommandSender): String {
        return "/trashcan"
    }

    override fun processCommand(
        iCommandSender: ICommandSender,
        strings: Array<out String?>
    ) {
        if (iCommandSender is IOpenTrashCan) {
            iCommandSender.openTrashCan()
        }
    }
}