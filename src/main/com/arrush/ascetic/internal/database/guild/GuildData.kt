package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.AsceticBot

data class GuildData (val id: Long, var prefix: String = AsceticBot.getPrefix(), var language: String = "en") {

    fun changePrefix(newPrefix: String): GuildData {
        this.prefix = newPrefix
        AsceticBot.guildDb.modify(this)
        return this
    }
}