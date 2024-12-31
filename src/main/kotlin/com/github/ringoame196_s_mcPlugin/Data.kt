package com.github.ringoame196_s_mcPlugin

import de.maxhenkel.voicechat.api.Group
import de.maxhenkel.voicechat.api.VoicechatServerApi
import org.bukkit.ChatColor

object Data {
    val TRANSCEIVER_ITEM_NAME = "${ChatColor.GREEN}トランシーバー"
    const val DEFAULT_GROUP_NAME = "transceiver"
    val groups = mutableMapOf<String, Group>()
    var api: VoicechatServerApi? = null
}
