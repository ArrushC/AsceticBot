package com.arrush.database

import me.arrush.javabase.databases.Database
import me.arrush.javabase.entities.Column
import me.arrush.javabase.entities.Table
import me.arrush.javabase.enums.DataType

@Suppress("MemberVisibilityCanBePrivate")
class DatabaseManager {
    val database get() = Database.withPostgres(5430, "postgres", "Arrush", "Password")!!
    val tables get() = mutableMapOf<String, Table>()

    private fun addTable(name: String, vararg columns: Column) {
        this.tables[name] = Table.TableCreator(name)
                .addColumns(columns.asList())
                .build(this.database)

        /**
         * This allows new columns to be added even if they exist.
         *
         * This part of the method will be removed once we have enough
         * columns
         */
        this.tables[name]!!.create().modifyStatement {
            // this is used because the `create` method has a pre-defined statement which is formatted.
            it.replace("IF NOT EXISTS", "")
        }.queue(null) {
            this.tables[name]!!.drop().queue()
            this.addTable(name, *columns)
        }
    }

    fun addGuildsTable() {
        this.addTable("Guilds",
                Column("id", DataType.INTEGER),
                Column("prefix", DataType.VARCHAR),
                Column("isPremium", DataType.BOOLEAN)
        )
    }

    fun addModTable() {
        // empty for now.
        // Need following columns: id, actiontype, reason, date.
    }

    fun addUsersTable() {
        
        this.addTable("Users",
                Column("id", DataType.INTEGER),
                Column("exp", DataType.INTEGER),
                Column("isPremium", DataType.BOOLEAN)
        )
    }

    fun addGuild(id: Long, isPremium: Boolean) {
        // Parse values from
    }

    fun addUser() {

    }
}