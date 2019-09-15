package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.AsceticBot

data class GuildData (val id: Long, var prefix: String = AsceticBot.INSTANCE.getPrefix(), var language: String = "en") {

    fun changePrefix(newPrefix: String): GuildData {
        this.prefix = newPrefix
        AsceticBot.INSTANCE.guildDb.modify(this)
        return this
    }
}