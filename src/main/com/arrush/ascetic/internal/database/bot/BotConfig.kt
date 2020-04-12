package com.arrush.ascetic.internal.database.bot

import de.arraying.kotys.JSON
import de.arraying.kotys.JSONField
import java.io.File

class BotConfig constructor(@JSONField(key = "prefix") var prefix: String,
                @JSONField(key = "token") var token: String,
                @JSONField(key = "version") var version: String,
                @JSONField(key = "is_lockdown") var isLockdown: Boolean,
                @JSONField(key = "mention") var mention: String) {

    constructor(): this("", "", "", false,"")

    companion object {
        fun from(file: String = "config.json"): BotConfig = JSON(File(file)).marshal(BotConfig::class.java)
    }
}