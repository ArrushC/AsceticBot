package com.arrush.database.guild

import discord4j.core.`object`.entity.Guild

enum class GuildDatabase {
    INSTANCE;

    private val guilds: Map<Long, GuildData> = mutableMapOf()


    fun addGuild(id: Long, guild: Guild) {

        // add database here.


    }
}