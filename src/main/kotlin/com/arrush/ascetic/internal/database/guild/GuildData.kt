package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.Constants
import me.arrush.javabase.annotations.SQLColumn

data class GuildData (@SQLColumn(column="id") val guildId: Long) {
    @SQLColumn(column="prefix") var prefix: String = Constants.PREFIX.getString()
}