package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants

data class GuildData (val id: Long, var prefix: String = Constants.PREFIX.getString()) {

    fun changePrefix(newPrefix: String): GuildData {
        this.prefix = newPrefix
        AsceticBot.INSTANCE.guildDb.modify(this)
        return this
    }
}