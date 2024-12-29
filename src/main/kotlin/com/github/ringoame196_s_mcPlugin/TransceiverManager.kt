package com.github.ringoame196_s_mcPlugin

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TransceiverManager {
    fun createItem(number: String): ItemStack {
        val item = ItemStack(Material.SLIME_BALL)
        val itemMeta = item.itemMeta
        itemMeta?.setDisplayName(Data.ITEM_NAME)
        itemMeta?.lore = mutableListOf(number)
        itemMeta?.setCustomModelData(Data.CUSTOM_MODEL_DATA)
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
}
