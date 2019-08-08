package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import discord4j.core.`object`.util.Snowflake
import me.arrush.javabase.entities.Column
import me.arrush.javabase.entities.Table
import me.arrush.javabase.enums.DataType

@Suppress("MemberVisibilityCanBePrivate")
enum class GuildDatabase {
    INSTANCE;

    val columns: Map<String, Column> = mapOf(
            "id" to Column("id", DataType.BIGINT)
    )
    val guildTable: Table = Table.TableCreator("Guilds").addColumns(columns.values).build(AsceticBot.INSTANCE.database)
    val guilds: MutableMap<Long, GuildData> = mutableMapOf()


    fun addGuild(id: Long) {
        this.guilds.putIfAbsent(id, GuildData(id, Constants.PREFIX.getString()))
    }

    fun getGuild(id: Snowflake): GuildData {
        val gData = GuildData(id.asLong())
        this.guildTable.select().withStatement("SELECT * FROM Guilds WHERE id == ${id.asLong()}")
                .fetch().parse(gData.javaClass)
        return gData
    }




}