package com.arrush.ascetic.internal.database.user

import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.database.AbstractDatabase
import discord4j.core.`object`.entity.User
import me.arrush.javabase.annotations.SQLColumn
import me.arrush.javabase.databases.Database
import me.arrush.javabase.entities.Column
import me.arrush.javabase.entities.Table
import java.util.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

class UserDatabase(database: Database): AbstractDatabase<UserData, User>() {

    val tableName: String = "Users"
    val columns: Map<String, Column> = Constants.getUserDbColumns()
    val users: MutableMap<Long, UserData> = mutableMapOf()
    val userTable: Table

    init {
        this.userTable = Table.TableCreator(this.tableName).addColumns(columns.values).buildAndCreate(database)
    }

    override fun loadData() {
        val rows = Objects.requireNonNull(this.userTable.select().withStatement("SELECT * FROM ${this.tableName}").fetchMultiple())
        for (row in rows) {
            val gd = UserData(row.getLong("id"))
            row.parse(gd)
            this.users.putIfAbsent(row.getLong("id"), gd)
        }
    }

    override fun modify(data: UserData) {
        val fieldValues: Map<String, Any> = UserData::class.declaredMemberProperties.filter { it.findAnnotation<SQLColumn>() != null }
                .map { it.findAnnotation<SQLColumn>()!!.column to it.get(data)!! }.toMap()
        this.userTable.update().withStatement(this.updateStatement(fieldValues)).queue()
        this.users.replace(data.userId, data)
    }

    override fun save(entity: User): Boolean {
        if (entity.isBot || this.users.contains(entity.id.asLong())) return false
        val data = UserData(entity.id.asLong())
        this.userTable.insert().withStatement(this.insertStatement(data)).queue()
        this.users.putIfAbsent(entity.id.asLong(), data)
        return true
    }

    private fun updateStatement(fieldValues: Map<String, Any>) = "UPDATE ${this.tableName} SET ${fieldValues.entries.joinToString { it.key + "=" + it.value }} WHERE id == ${fieldValues["id"]}"
    private fun insertStatement(data: UserData): String = "INSERT INTO ${this.tableName} VALUES (${data.userId}, ${data.exp}, ${data.isPremium})"
}