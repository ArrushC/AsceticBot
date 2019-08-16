package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.database.AbstractDatabase
import discord4j.core.`object`.util.Snowflake
import me.arrush.javabase.databases.Database
import me.arrush.javabase.entities.Table
import java.util.*


class GuildDatabase(database: Database) : AbstractDatabase<GuildData, Snowflake>("Guilds", Constants.getGuildDbColumns()) {
    val guilds: MutableMap<Long, GuildData> = mutableMapOf()

    val guildTable: Table = Table.TableCreator(this.tableName).addColumns(this.columns.values).buildAndCreate(database)

    override fun loadData() {
        val rows = Objects.requireNonNull(this.guildTable.select().withStatement("SELECT * FROM ${this.tableName}").fetchMultiple())
        for (row in rows) {
            val id = row.getLong("id")
            val gd = GuildData(id, row.getString("prefix"))
            this.guilds.putIfAbsent(id, gd)
        }
    }

    // each method in GuildData will return the instance itself so it will be easier to modify.
    override fun modify(data: GuildData) {
        val fieldValues: LinkedHashSet<Pair<String, Any>> = linkedSetOf(
                "prefix" to data.prefix
        )
        this.guildTable.update().withStatement(this.updateStatement(data.id, fieldValues)).queue()
        this.guilds.replace(data.id, data)
    }

    // Saving guilds that do not exist in the database.
    override fun save(entity: Snowflake) {
        this.saveMultiple(listOf(entity))
    }

    override fun saveMultiple(entities: List<Snowflake>) {
        val bulk = mutableListOf<String>()
        for (id in entities.map {it.asLong()}) {
            if (this.guilds.contains(id)) return
            val data = GuildData(id)
            bulk.add("(${data.id}, \'${data.prefix}\')")
            this.guilds.putIfAbsent(id, data)
        }
        this.guildTable.insert().withStatement("INSERT INTO ${this.tableName} VALUES ${bulk.joinToString()}").queue()
    }
}
