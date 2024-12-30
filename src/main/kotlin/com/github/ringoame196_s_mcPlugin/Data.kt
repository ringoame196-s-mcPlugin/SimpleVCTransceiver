package com.github.ringoame196_s_mcPlugin

import de.maxhenkel.voicechat.api.Group
import de.maxhenkel.voicechat.api.VoicechatServerApi
import org.bukkit.ChatColor
import java.util.UUID

object Data {
    val TRANSCEIVER_ITEM_NAME = "${ChatColor.GREEN}トランシーバー"
    const val DEFAULT_GROUP_NAME = "transceiver"
    val group_password = UUID.randomUUID().toString()
    val groups = mutableMapOf<String, Group>()
    const val CUSTOM_MODEL_DATA = 1
    var api: VoicechatServerApi? = null
}
