package com.arrush.ascetic.internal.database.guild

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.database.IDatabase
import discord4j.core.event.domain.lifecycle.ReadyEvent
import me.arrush.javabase.annotations.SQLColumn
import me.arrush.javabase.entities.Column
import me.arrush.javabase.entities.Row
import me.arrush.javabase.entities.Table
import me.arrush.javabase.enums.DataType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

@Suppress("MemberVisibilityCanBePrivate")
enum class GuildDatabase: IDatabase<GuildData, ReadyEvent.Guild> {

    // The instance.
    INSTANCE {
        // This simply loads data from the database, preventing overwritten data
        // It is void to retain immutability of the map.
        override fun loadData() {
            val rows: List<Row> = this.guildTable.select().withStatement("SELECT * FROM ${this.tableName}").fetchMultiple()
            for (row in rows) {
                val gd = GuildData(row.getLong("id"))
                row.parse(gd.javaClass)
                this.guilds.putIfAbsent(row.getLong("id"), gd)
            }
        }

        // each method in GuildData will return the instance itself so it will be easier to modify.
        override fun modify(data: GuildData) {
            val fieldValues: Map<String, Any> = GuildData::class.declaredMemberProperties.filter { it.findAnnotation<SQLColumn>() != null }
                    .map { it.findAnnotation<SQLColumn>()!!.column to it.get(data)!! }.toMap() // Map<ColumnName, ColumnValue>
            this.guildTable.update().withStatement(this.updateStatement(fieldValues)).queue()
            this.guilds.replace(data.guildId, data)
        }

        // Saving guilds that do not exist in the database.
        override fun save(entity: ReadyEvent.Guild) {
            if (!entity.isAvailable) return
            val data = GuildData(entity.id.asLong(), Constants.PREFIX.getString())
            this.guildTable.insert().withStatement(this.insertStatement(data)).queue()
            this.guilds.putIfAbsent(entity.id.asLong(), data)
        }
    };

    val tableName: String = "Guilds"
    val columns: Map<String, Column> = mapOf(
            "id" to Column("id", DataType.BIGINT),
            "prefix" to Column("prefix", DataType.TEXT)
    )
    val guildTable: Table = Table.TableCreator(this.tableName).addColumns(columns.values).build(AsceticBot.INSTANCE.database)
    val guilds: MutableMap<Long, GuildData> = mutableMapOf()



    protected fun updateStatement(fieldValues: Map<String, Any>) = "UPDATE ${this.tableName} SET ${fieldValues.entries.joinToString { it.key + "=" + it.value }} WHERE id == ${fieldValues["id"]}"
    protected fun insertStatement(data: GuildData): String = "INSERT INTO ${this.tableName} VALUES (${data.guildId}, ${data.prefix})"
}