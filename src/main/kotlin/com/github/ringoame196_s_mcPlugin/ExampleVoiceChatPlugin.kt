package com.github.ringoame196_s_mcPlugin

import de.maxhenkel.voicechat.api.VoicechatApi
import de.maxhenkel.voicechat.api.VoicechatPlugin
import de.maxhenkel.voicechat.api.events.EventRegistration
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
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
        registration?.registerEvent(MicrophonePacketEvent::class.java, this::onMicrophonePacket)
    }

    private fun onMicrophonePacket(e: MicrophonePacketEvent) {
        val transceiverManager = TransceiverManager()
        e.senderConnection ?: return
        val api = e.voicechat ?: return
        val player = e.senderConnection?.player?.player as? Player ?: return
        val offHandItem = player.inventory.itemInOffHand.clone()
        offHandItem.amount = 1
        transceiverManager.acquisitionTransceiverNumber(offHandItem) ?: return
        val listenMessage = "${ChatColor.GOLD}[トランシーバー] ${player.name}が発言中"
        e.cancel()

        var c = 0
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) continue
            val onlinePlayerOffHandItem = onlinePlayer.inventory.itemInOffHand.clone()
            onlinePlayerOffHandItem.amount = 1
            if (offHandItem != onlinePlayerOffHandItem) continue
            val onlineConnection = api.getConnectionOf(onlinePlayer.uniqueId) ?: continue
            api.sendStaticSoundPacketTo(onlineConnection, e.packet.toStaticSoundPacket())
            onlinePlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(listenMessage))
            c ++
        }
        val speakerMessage = "${ChatColor.YELLOW}[トランシーバー] ${c}人が聞いています"
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(speakerMessage))
    }
}
