package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.database.AbstractDatabase
import discord4j.core.event.domain.lifecycle.ReadyEvent
import me.arrush.javabase.annotations.SQLColumn
import me.arrush.javabase.databases.Database
import me.arrush.javabase.entities.Column
import me.arrush.javabase.entities.Table
import java.util.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class GuildDatabase(database: Database) : AbstractDatabase<GuildData, ReadyEvent.Guild>() {

    val tableName: String = "Guilds"
    val columns: Map<String, Column> = Constants.getGuildDbColumns()
    val guilds: MutableMap<Long, GuildData> = mutableMapOf()
    val guildTable: Table

    init {
        this.guildTable = Table.TableCreator(this.tableName).addColumns(this.columns.values).buildAndCreate(database)
    }

    // This simply loads data from the database, preventing overwritten data
    // It is void to retain immutability of the map.
    override fun loadData() {
        val rows = Objects.requireNonNull(this.guildTable.select().withStatement("SELECT * FROM ${this.tableName}").fetchMultiple())
        for (row in rows) {
            val gd = GuildData(row.getLong("id"))
            row.parse(gd)
            this.guilds.putIfAbsent(row.getLong("id"), gd)
        }
    }

    // each method in GuildData will return the instance itself so it will be easier to modify.
    override fun modify(data: GuildData) {
        val fieldValues: Map<String, Any> = GuildData::class.memberProperties.filter { it.findAnnotation<SQLColumn>() != null }
                .map { it.findAnnotation<SQLColumn>()!!.column to it.get(data)!! }.toMap()
        this.guildTable.update().withStatement(this.updateStatement(fieldValues)).queue()
        this.guilds.replace(data.guildId, data)
    }

    // Saving guilds that do not exist in the database.
    override fun save(entity: ReadyEvent.Guild): Boolean {
        if (this.guilds.contains(entity.id.asLong())) return false
        val data = GuildData(entity.id.asLong())
        this.guildTable.insert().withStatement(this.insertStatement(data)).queue()
        this.guilds.putIfAbsent(entity.id.asLong(), data)
        return true
    }



    private fun updateStatement(fieldValues: Map<String, Any>) = "UPDATE ${this.tableName} SET ${fieldValues.entries.joinToString { it.key + "=" + it.value }} WHERE id == ${fieldValues["id"]}"
    private fun insertStatement(data: GuildData): String = "INSERT INTO ${this.tableName} VALUES (${data.guildId}, \'${data.prefix}\')"
}