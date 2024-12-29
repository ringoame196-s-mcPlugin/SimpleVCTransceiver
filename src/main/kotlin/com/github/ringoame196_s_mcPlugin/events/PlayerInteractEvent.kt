package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.Data
import com.github.ringoame196_s_mcPlugin.TransceiverManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractEvent : Listener {
    private val transceiverManager = TransceiverManager()

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInOffHand
        val itemMeta = item.itemMeta ?: return
        val itemName = itemMeta.displayName
        val lore = itemMeta.lore ?: return
        val number = lore[0]?.toString() ?: return
        if (!player.isSneaking) return
        if (itemName != Data.ITEM_NAME) return
        if (e.action != Action.RIGHT_CLICK_AIR) return

        // トランシーバーの受信メッセージ
        val message = "${ChatColor.GOLD}[トランシーバー] ${number}番通信を受信！"
        val sound = Sound.BLOCK_NOTE_BLOCK_PLING
        var c = 0

        for (player in Bukkit.getOnlinePlayers()) {
            if (!transceiverManager.isHaveItem(player, number)) continue
            player.playSound(player, sound, 1f, 1f)
            player.sendTitle("", message)
            c++
        }

        val sendMessage = "${ChatColor.AQUA}${c}人に通信を飛ばしました"
        player.sendMessage(sendMessage)
    }
}
