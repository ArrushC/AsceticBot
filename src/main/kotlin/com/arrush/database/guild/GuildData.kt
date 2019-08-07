package com.arrush.database.guild

import me.arrush.javabase.annotations.SQLColumn

class GuildData (@SQLColumn(column="id") val guildId: Long,
                 @SQLColumn(column="prefix") var prefix: String,
                 @SQLColumn(column="isPremium") val isPremium: Boolean )