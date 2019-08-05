package com.arrush.ascetic.listeners.eschedule

import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.Event
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.stream.Collector
import java.util.stream.Collectors
import kotlin.streams.toList
import kotlin.system.exitProcess

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class EventScheduler<E : Event>(private val eventType: Class<E>) {
    private var id = 0
    private val events: MutableSet<ScheduledEvent<E>>
    private val eventRemovers: ScheduledExecutorService

    init {
        this.events = HashSet()
        this.eventRemovers = Executors.newSingleThreadScheduledExecutor()
    }

    fun onGenericEvent(event: Event) {
        if (event.javaClass != this.eventType) return
        try {
            this.events.removeAll(this.events.stream().filter { e ->
                try {
                    return@filter e.scheduleEvent(event as E).status == ScheduleStatus.Status.COMPLETED
                } catch (e1: Exception) {
                    e1.printStackTrace()
                    return@filter false
                }
            }.filter { return@filter ((it is ChainedScheduledEvent) && (it.position == it.scheduledEvents.size)) }
                    .collect(Collectors.toSet<ScheduledEvent<E>>() as Collector<in ScheduledEvent<E>, Any, Set<ScheduledEvent<E>>>?))
        } catch (e: ConcurrentModificationException) {
            println("Terminating program as an event-schedule entity has an empty body.")
            exitProcess(401)
        }
    }

    fun getScheduledEvent(condition: (ScheduledEvent<out Event>) -> Boolean) = this.events.stream().filter(condition).toList()

    fun scheduleEvent(tag: String, guildId: Snowflake, operation: (E) -> ScheduleStatus, timeoutOp: () -> Unit, timeout: Long, timeUnit: TimeUnit) {
        this.scheduleEvent(object: ScheduledEvent<E>(tag, guildId, timeout, timeUnit) {
            override fun scheduleEvent(event: E): ScheduleStatus {
                return operation(event)
            }

            override fun onTimeout() {
                timeoutOp()
            }
        })
    }

    fun scheduleEvent(scheduleedEvent: ScheduledEvent<E>) {
        this.events.add(scheduleedEvent)
        if (scheduleedEvent.timeout > 0) { // limited time.
            eventRemovers.schedule({
                events.remove(scheduleedEvent)
                scheduleedEvent.onTimeout()
            }, scheduleedEvent.timeout, scheduleedEvent.timeoutUnit)
        }
    }

    fun scheduleEvent(tag: String, guildId: Snowflake, operation: (E) -> ScheduleStatus) {
        this.scheduleEvent(object : ScheduledEvent<E>(tag, guildId) {

            @Throws(Exception::class)
            override fun scheduleEvent(event: E): ScheduleStatus {
                return operation(event)
            }
        })
    }


    fun scheduleChainedEvents(intervalCondition: (E) -> ScheduleStatus, vararg scheduledEvents: (E) -> ScheduleStatus) {
        this.events.add(ChainedScheduledEvent(intervalCondition, scheduledEvents))
    }
}