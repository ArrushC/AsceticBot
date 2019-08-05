package com.arrush.ascetic

import com.arrush.logger.Logger
import discord4j.core.`object`.util.Snowflake
import java.awt.Color

@Suppress("unused")
enum class Constants(private val value: Any) {
    PREFIX("->"),
    VERSION("1.3.0"),
    IS_LOCKDOWN(false),
    MENTION("<@607528672898842644>"),
    COLOUR(Color(255, 6, 6)),
    DEV_IDS(mapOf(Pair("Arrush", Snowflake.of(500360024242126849)), Pair("BloodyDev", Snowflake.of(270672369675272206)))), // first is mine next is bloody dev's.
    LOGGER(Logger("AsceticBot"));

    fun get() = value
    fun getMap(): Map<*, *> = this.value as Map<*, *>
    fun getList(): List<*> = this.value as List<*>
    fun getString(): String = this.value as String
    fun getInteger(): Int = this.value as Int
    fun getLong(): Long = this.value as Long
    fun getBoolean(): Boolean = this.value as Boolean
}