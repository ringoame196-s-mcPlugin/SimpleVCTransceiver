package com.github.ringoame196_s_mcPlugin

import de.maxhenkel.voicechat.api.Group
import de.maxhenkel.voicechat.api.VoicechatApi
import de.maxhenkel.voicechat.api.VoicechatPlugin
import de.maxhenkel.voicechat.api.VoicechatServerApi
import de.maxhenkel.voicechat.api.events.EventRegistration
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ExampleVoiceChatPlugin : VoicechatPlugin {
    /**
     * @return the unique ID for this voice chat plugin
     */
    override fun getPluginId(): String {
        return "SimpleVCTransceiver"
    }

    /**
     * Called when the voice chat initializes the plugin.
     *
     * @param api the voice chat API
     */

    override fun initialize(api: VoicechatApi?) {
    }

    /**
     * Called once by the voice chat to register all events.
     *
     * @param registration the event registration
     */

    override fun registerEvents(registration: EventRegistration?) {
        registration?.registerEvent(MicrophonePacketEvent::class.java, this::onMicrophone)
    }

    private fun onMicrophone(e: MicrophonePacketEvent) {
        val bukkitPlayer = e.senderConnection?.player?.player as Player
        val offhandItem = bukkitPlayer.inventory.itemInOffHand
        val offhandItemMeta = offhandItem.itemMeta
        val itemName = Data.ITEM_NAME

        val api = e.voicechat
        val transceiverNumber = offhandItemMeta?.lore?.get(0)?.toString()
        var transceiverGroup = if (transceiverNumber != null) {
            acquisitionGroup(api, transceiverNumber)
        } else {
            null
        }
        if (offhandItemMeta?.displayName != itemName) transceiverGroup = null
        val connection = api.getConnectionOf(bukkitPlayer.uniqueId) ?: return

        val connectionGroup = connection.group

        if (connectionGroup != transceiverGroup) {
            val message = "${ChatColor.YELLOW}トランシーバーを切り替えました"
            connection.group = transceiverGroup
            bukkitPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
        }
    }

    private fun acquisitionGroup(api: VoicechatServerApi, number: String): Group {
        val groupName = "${Data.DEFAULT_GROUP_NAME}_$number"
        return Data.groups[groupName] ?: createGroup(api, groupName)
    }

    private fun createGroup(api: VoicechatServerApi, groupName: String): Group {
        val groupPass = Data.group_password

        val group = api.groupBuilder()
            .setPersistent(true) // Doesn't remove the group once all players left
            .setName(groupName) // The name of the group
            .setPassword(groupPass) // The password of the group
            .setType(Group.Type.NORMAL) // The type of the group (NORMAL, OPEN, ISOLATED)
            .build()

        Data.groups[groupName] = group
        return group
    }
}
