package com.arrush.ascetic.listeners.eschedule

import com.arrush.ascetic.utility.emptySnowflake
import discord4j.core.event.domain.Event

@Suppress("MemberVisibilityCanBePrivate")
open class ChainedScheduledEvent<E: Event>(val intervalCondition: (E) -> ScheduleStatus, val scheduledEvents: Array<out (E) -> ScheduleStatus>): ScheduledEvent<E>("", emptySnowflake()) {
    var position: Int = 0

    override fun scheduleEvent(event: E): ScheduleStatus {
        if (!intervalCondition(event).isCompleted()) return ScheduleStatus(ScheduleStatus.Status.NOT_COMPLETED)
        val result = this.scheduledEvents[position](event)
        if (result.isCompleted()) this.position += 1
        return result
    }

}