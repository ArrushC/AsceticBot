package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.lifecycle.ReadyEvent
import me.arrush.javabase.entities.Column
import me.arrush.javabase.entities.Row
import me.arrush.javabase.entities.Table
import me.arrush.javabase.enums.DataType

@Suppress("MemberVisibilityCanBePrivate")
enum class GuildDatabase {
    INSTANCE;

    val tableName: String = "Guilds"
    val columns: Map<String, Column> = mapOf(
            "id" to Column("id", DataType.BIGINT)
    )
    val guildTable: Table = Table.TableCreator(this.tableName).addColumns(columns.values).build(AsceticBot.INSTANCE.database)
    val guilds: MutableMap<Long, GuildData> = mutableMapOf()


    fun addGuild(id: Long) {
        this.guilds.putIfAbsent(id, GuildData(id, Constants.PREFIX.getString()))
    }

    fun getGuild(id: Snowflake): GuildData {
        val gData = GuildData(id.asLong())
        this.guildTable.select().withStatement("SELECT * FROM ${this.tableName} WHERE id == ${id.asLong()}")
                .fetch().parse(gData.javaClass)
        return gData
    }

    fun loadData(): MutableMap<Long, GuildData> {
        val rows: List<Row> = this.guildTable.select().withStatement("SELECT * FROM ${this.tableName}").fetchMultiple()
        for (row in rows) {
            val gd = GuildData()
            row.parse(gd.javaClass)
            this.guilds.putIfAbsent(row.getLong("id"), gd)
        }
        return this.guilds
    }

    fun saveGuild(guild: ReadyEvent.Guild) {
        if (!guild.isAvailable) return
        // save it in to the actual database.
        // replace this statement with a query.
        this.guilds.putIfAbsent(guild.id.asLong(), GuildData(guild.id.asLong(), Constants.PREFIX.getString()))
    }
    
    fun saveGuilds(guilds: Collection<ReadyEvent.Guild>) {
        for (guild in guilds) {
            this.saveGuild(guild)
        }
    }

    // function for modifying data.




}