package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.Data
import com.github.ringoame196_s_mcPlugin.TransceiverManager
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

class ChangeTransceiverEvent : Listener {
    private val transceiverManager = TransceiverManager()

    @EventHandler
    fun onPlayerSwapHandItems(e: PlayerSwapHandItemsEvent) {
        val player = e.player
        val mainHandItem = e.mainHandItem
        val mainHandItemName = mainHandItem?.itemMeta?.displayName
        val offHandItem = e.offHandItem
        val offHandITemName = offHandItem?.itemMeta?.displayName
        val transceiverItemName = Data.TRANSCEIVER_ITEM_NAME

        when (transceiverItemName) {
            offHandITemName -> transceiverManager.joinGroup(player, transceiverManager.acquisitionGroupNumber(offHandItem) ?: return)
            mainHandItemName -> transceiverManager.leaveGroup(player)
            else -> return
        }

        val message = "${ChatColor.YELLOW}トランシーバーの切り替え"
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val offHandSlot = 40
        if (e.slot != offHandSlot) return
        if (e.clickedInventory != player.inventory) return

        val transceiverItemName = Data.TRANSCEIVER_ITEM_NAME
        val clickItem = e.cursor
        val offHandItem = player.inventory.itemInOffHand

        if (clickItem?.itemMeta?.displayName == transceiverItemName) {
            transceiverManager.joinGroup(player, transceiverManager.acquisitionGroupNumber(clickItem) ?: return)
        } else if (offHandItem.itemMeta?.displayName == transceiverItemName) {
            transceiverManager.leaveGroup(player)
        }
    }
}
