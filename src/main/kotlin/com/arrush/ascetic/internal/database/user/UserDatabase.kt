package com.arrush.ascetic.internal.database.user

import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.database.AbstractDatabase
import discord4j.core.`object`.util.Snowflake
import me.arrush.javabase.databases.Database
import me.arrush.javabase.entities.Table
import java.util.*

class UserDatabase(database: Database): AbstractDatabase<UserData, Snowflake>("Users", Constants.getUserDbColumns()) {

    val users: MutableMap<Long, UserData> = mutableMapOf()
    val userTable: Table = Table.TableCreator(this.tableName).addColumns(this.columns.values).buildAndCreate(database)

    override fun loadData() {
        val rows = Objects.requireNonNull(this.userTable.select().withStatement("SELECT * FROM ${this.tableName}").fetchMultiple())
        for (row in rows) {
            val id = row.getLong("id")
            val gd = UserData(id,
                    row.getLong("exp"),
                    row.getLong("level"),
                    row.getBoolean("ispremium"))
            this.users.putIfAbsent(id, gd)
        }
    }

    override fun modify(data: UserData) {
        val fieldValues: LinkedHashSet<Pair<String, Any>> = linkedSetOf(
                "exp" to data.exp,
                "level" to data.lvl,
                "ispremium" to data.isPremium
        )
        this.userTable.update().withStatement(this.updateStatement(data.id, fieldValues)).queue()
        this.users.replace(data.id, data)
    }

    override fun save(entity: Snowflake) {
        this.saveMultiple(listOf(entity))
    }

    override fun saveMultiple(entities: List<Snowflake>) {
        val bulk = mutableListOf<String>()
        for (id in entities.map {it.asLong()}) {
            if (this.users.contains(id)) return
            val data = UserData(id)
            bulk.add("(${data.id}, ${data.exp}, ${data.lvl}, ${data.isPremium})")
            this.users.putIfAbsent(id, data)
        }
        this.userTable.insert().withStatement("INSERT INTO ${this.tableName} VALUES ${bulk.joinToString()}").queue()
    }



}