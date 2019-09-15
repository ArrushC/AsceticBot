package com.arrush.ascetic.utility

import discord4j.core.`object`.util.Snowflake
import org.reflections.Reflections
import kotlin.streams.toList

fun <T>  Reflections.getMappedSubTypes(type: Class<T>): List<T> = this.getSubTypesOf(type).stream().map {
    println("${it.simpleName} has been registered!")
    it.newInstance() }.toList()

fun emptySnowflake(): Snowflake = Snowflake.of(0)

