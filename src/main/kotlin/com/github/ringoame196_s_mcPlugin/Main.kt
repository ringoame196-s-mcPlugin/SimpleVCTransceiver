package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.events.ChangeTransceiverEvent
import com.github.ringoame196_s_mcPlugin.events.PlayerInteractEvent
import de.maxhenkel.voicechat.api.BukkitVoicechatService
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    private val logger = Bukkit.getLogger()
    private lateinit var voicechatPlugin: ExampleVoiceChatPlugin
    override fun onEnable() {
        super.onEnable()

        val service: BukkitVoicechatService? = server.servicesManager.load(BukkitVoicechatService::class.java)

        if (service != null) {
            voicechatPlugin = ExampleVoiceChatPlugin()
            service.registerPlugin(voicechatPlugin)
            logger.info("Successfully registered example plugin")
        } else {
            logger.info("Failed to register example plugin")
        }

        val command = getCommand("transceiver")
        command!!.setExecutor(Command())
        command.tabCompleter = TabCompleter()
        server.pluginManager.registerEvents(PlayerInteractEvent(), this)
        server.pluginManager.registerEvents(ChangeTransceiverEvent(), this)
    }
}
