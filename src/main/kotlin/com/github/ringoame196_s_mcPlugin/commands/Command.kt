package com.github.ringoame196_s_mcPlugin

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Command : CommandExecutor {
    private val transceiverManager = TransceiverManager()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        if (args.isEmpty()) return false
        val number = args[0]
        val item = transceiverManager.createItem(number)
        val message = "${ChatColor.GOLD}${number}番のトランシーバーを生成しました"
        sender.inventory.addItem(item)
        sender.sendMessage(message)

        return true
    }
}
