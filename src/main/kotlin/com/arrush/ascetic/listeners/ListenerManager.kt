package com.arrush.ascetic.listeners

import discord4j.core.event.domain.Event
import discord4j.core.event.domain.guild.GuildCreateEvent
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import java.util.*
import kotlin.streams.toList

class ListenerManager {
    private val parentMethods = Arrays.stream(IListener::class.java.declaredMethods).map<String> { it.name }.toList()
    private val listeners: List<IListener>

    init {
        this.listeners = getListeners()
    }

    //private fun getListeners(): List<IListener> = Reflections(ListenerManager::class.java.`package`.name).getMappedSubTypes(IListener::class.java)
    private fun getListeners(): List<IListener> = listOf(BotListener(), MessageListener())

    fun fireListeners(event: Event) {
        when (event) {
            is MessageCreateEvent ->  listeners.forEach { it.onMessageCreate(event) }
            is ReadyEvent ->  listeners.forEach { it.onBotReady(event) }
            is GuildCreateEvent -> listeners.forEach {it.onGuildCreate(event)}
            else -> listeners.forEach { it.onGenericEvent(event) }
        }
    }

    private fun slowMethod(event: Event) {
        for (listener in this.listeners) {
            Arrays.stream(listener.javaClass.declaredMethods).filter { m -> m.parameters.size == 1 }
                    .filter { m -> m.parameters[0].type == event.javaClass }
                    .filter { parentMethods.contains(it.name) }.forEach { it.invoke(listener.javaClass.newInstance(), event) }
        }
    }
}
