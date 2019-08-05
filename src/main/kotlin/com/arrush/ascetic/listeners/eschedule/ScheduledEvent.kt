package com.arrush.ascetic.listeners.eschedule

import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.Event
import java.util.concurrent.TimeUnit

abstract class ScheduledEvent<E: Event>(val tag: String, val guildId: Snowflake, var timeout: Long = -1, var timeoutUnit: TimeUnit = TimeUnit.SECONDS) {


    abstract fun scheduleEvent(event: E): ScheduleStatus

    open fun onTimeout() {}

    fun setTimeout(timeout: Long): ScheduledEvent<E> {
        this.timeout = timeout
        return this
    }

    fun setTimeoutUnit(timeoutUnit: TimeUnit): ScheduledEvent<E> {
        this.timeoutUnit = timeoutUnit
        return this
    }
}