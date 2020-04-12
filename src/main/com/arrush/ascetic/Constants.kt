package com.arrush.ascetic

import com.arrush.ascetic.internal.logger.Logger
import discord4j.core.`object`.util.Snowflake
import me.arrush.javabase.entities.Column
import me.arrush.javabase.enums.DataType
import java.awt.Color

@Suppress("unused", "UNCHECKED_CAST")
enum class Constants(protected val value: Any) {
    COLOUR(Color(255, 6, 6)),
    DEV_IDS(mapOf("Arrush" to Snowflake.of(500360024242126849), "BloodyDev" to Snowflake.of(270672369675272206))), // first is mine next is bloody dev's.
    LOGGER(Logger("AsceticBot")),
    PREMIUM_ROLEID(596346917705875470),
    PACKAGE_COMMAND("com.arrush.ascetic.commands"),
    START_CMD(listOf(""));

    companion object {
        fun getLogger(): Logger = LOGGER.get() as Logger
        fun getDevIds(): Map<String, Snowflake> = DEV_IDS.get() as Map<String, Snowflake>
        fun getGuildDbColumns(): LinkedHashMap<String, Column> = linkedMapOf(
                "id" to Column("id", DataType.BIGINT),
                "prefix" to Column("prefix", DataType.TEXT),
                "language" to Column("language", DataType.TEXT)
        )
        fun getUserDbColumns(): LinkedHashMap<String, Column> = linkedMapOf(
                "id" to Column(  "id", DataType.BIGINT),
                "exp" to Column("exp", DataType.BIGINT),
                "level" to Column("level", DataType.BIGINT),
                "ispremium" to Column("ispremium", DataType.BOOLEAN)
        )
    }

    fun get() = this.value
    fun getMap(): Map<*, *> = this.value as Map<*, *>
    fun getList(): List<*> = this.value as List<*>
    fun getString(): String = this.value as String
    override fun toString(): String = this.value as String
    fun getInteger(): Int = this.value as Int
    fun getLong(): Long = this.value as Long
    fun getBoolean(): Boolean = this.value as Boolean
}