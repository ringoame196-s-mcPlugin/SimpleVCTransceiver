package com.github.ringoame196_s_mcPlugin

import de.maxhenkel.voicechat.api.Group
import de.maxhenkel.voicechat.api.VoicechatServerApi
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.UUID

class TransceiverManager {
    fun createItem(number: String): ItemStack {
        val item = ItemStack(Material.SLIME_BALL)
        val itemMeta = item.itemMeta
        itemMeta?.setDisplayName(Data.TRANSCEIVER_ITEM_NAME)
        itemMeta?.lore = mutableListOf(number)

        val customModelDataComponent = itemMeta?.customModelDataComponent
        customModelDataComponent?.strings = mutableListOf("transceiver")
        itemMeta?.setCustomModelDataComponent(customModelDataComponent)

        item.itemMeta = itemMeta
        return item
    }

    fun isHaveItem(player: Player, number: String): Boolean {
        val transceiverItem = createItem(number)
        for (hotBarNumber in 0..8) {
            val item = player.inventory.getItem(hotBarNumber)?.clone() ?: continue
            item.amount = 1
            if (item == transceiverItem) return true
        }
        return false
    }

    fun joinGroup(player: Player, number: String) {
        val api = Data.api

        if (api == null) {
            val message = "${ChatColor.RED}現在 トランシーバーの準備中です\n時間をあけてもう一度実行してください"
            player.sendMessage(message)
            return
        }

        val transceiverGroup = acquisitionGroup(api, number)
        val connection = api.getConnectionOf(player.uniqueId) ?: return
        connection.group = transceiverGroup
    }

    fun leaveGroup(player: Player) {
        val api = Data.api ?: return
        val connection = api.getConnectionOf(player.uniqueId) ?: return
        connection.group = null
    }

    private fun acquisitionGroup(api: VoicechatServerApi, number: String): Group {
        val groupName = "${Data.DEFAULT_GROUP_NAME}_$number"
        return Data.groups[groupName] ?: createGroup(api, groupName)
    }

    private fun createGroup(api: VoicechatServerApi, groupName: String): Group {
        val groupPass = UUID.randomUUID().toString()

        val group = api.groupBuilder()
            .setPersistent(true) // Doesn't remove the group once all players left
            .setName(groupName) // The name of the group
            .setPassword(groupPass) // The password of the group
            .setType(Group.Type.NORMAL) // The type of the group (NORMAL, OPEN, ISOLATED)
            .build()

        Data.groups[groupName] = group
        return group
    }

    fun acquisitionGroupNumber(item: ItemStack): String? {
        return item.itemMeta?.lore?.get(0)?.toString()
    }
}
