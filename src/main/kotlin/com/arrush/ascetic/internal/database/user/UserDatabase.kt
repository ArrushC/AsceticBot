package com.arrush.ascetic.internal.database.user

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.database.IDatabase
import discord4j.core.`object`.entity.User
import me.arrush.javabase.annotations.SQLColumn
import me.arrush.javabase.entities.Column
import me.arrush.javabase.entities.Row
import me.arrush.javabase.entities.Table
import me.arrush.javabase.enums.DataType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

enum class UserDatabase: IDatabase<UserData, User> {
    INSTANCE {
        override fun loadData() {
            val rows: List<Row> = this.userTable.select().withStatement("SELECT * FROM ${this.tableName}").fetchMultiple()
            for (row in rows) {
                val gd = UserData(row.getLong("id"))
                row.parse(gd.javaClass)
                this.users.putIfAbsent(row.getLong("id"), gd)
            }
        }

        override fun modify(data: UserData) {
            val fieldValues: Map<String, Any> = UserData::class.declaredMemberProperties.filter { it.findAnnotation<SQLColumn>() != null }
                    .map { it.findAnnotation<SQLColumn>()!!.column to it.get(data)!! }.toMap() // Map<ColumnName, ColumnValue>
            this.userTable.update().withStatement(this.updateStatement(fieldValues)).queue()
            this.users.replace(data.userId, data)
        }

        override fun save(entity: User) {
            if (entity.isBot) return
            val data = UserData(entity.id.asLong())
            this.userTable.insert().withStatement(this.insertStatement(data)).queue()
            this.users.putIfAbsent(entity.id.asLong(), data)
        }
    };

    val tableName: String = "Users"
    val columns: Map<String, Column> = mapOf(
            "id" to Column("id", DataType.BIGINT),
            "exp" to Column("exp", DataType.BIGINT),
            "isPremium" to Column("isPremium", DataType.BOOLEAN)
    )
    val userTable: Table = Table.TableCreator(this.tableName).addColumns(columns.values).build(AsceticBot.INSTANCE.database)
    val users: MutableMap<Long, UserData> = mutableMapOf()

    protected fun updateStatement(fieldValues: Map<String, Any>) = "UPDATE ${this.tableName} SET ${fieldValues.entries.joinToString { it.key + "=" + it.value }} WHERE id == ${fieldValues["id"]}"
    protected fun insertStatement(data: UserData): String = "INSERT INTO ${this.tableName} VALUES (${data.userId}, ${data.exp}, ${data.isPremium})"
}