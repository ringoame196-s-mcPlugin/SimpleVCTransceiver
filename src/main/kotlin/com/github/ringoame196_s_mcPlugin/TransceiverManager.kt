package com.github.ringoame196_s_mcPlugin

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TransceiverManager {
    private val transceiverItemType = Material.SLIME_BALL
    private val transceiverItemCustomModelData = "transceiver"

    fun createItem(number: String): ItemStack {
        val item = ItemStack(transceiverItemType)
        val itemMeta = item.itemMeta
        itemMeta?.setDisplayName(Data.TRANSCEIVER_ITEM_NAME)
        itemMeta?.lore = mutableListOf(number)

        val customModelDataComponent = itemMeta?.customModelDataComponent
        customModelDataComponent?.strings = mutableListOf(transceiverItemCustomModelData)
        itemMeta?.setCustomModelDataComponent(customModelDataComponent)

        item.itemMeta = itemMeta
        return item
    }

    private fun checkItem(item: ItemStack): Boolean {
        if (item.type != transceiverItemType) return false
        val itemMeta = item.itemMeta ?: return false
        val customModelDataComponent = itemMeta.customModelDataComponent ?: return false
        return customModelDataComponent.strings.contains(transceiverItemCustomModelData)
    }

    fun isHotBarHaveItem(player: Player, number: String): Boolean {
        val transceiverItem = createItem(number)
        for (hotBarNumber in 0..8) {
            val item = player.inventory.getItem(hotBarNumber)?.clone() ?: continue
            item.amount = 1
            if (item == transceiverItem) return true
        }
        return false
    }

    fun acquisitionTransceiverNumber(item: ItemStack): String? {
        if (!checkItem(item)) return null
        return item.itemMeta?.lore?.get(0)?.toString()
    }
}
