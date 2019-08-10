package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.Constants
import me.arrush.javabase.annotations.SQLColumn

class GuildData (@SQLColumn(column="id") val guildId: Long= 0, @SQLColumn(column="prefix") var prefix: String= Constants.PREFIX.getString()) {

}