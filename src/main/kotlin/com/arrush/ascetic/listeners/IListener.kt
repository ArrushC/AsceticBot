package com.arrush.ascetic.listeners

import discord4j.core.event.domain.Event
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent

interface IListener {

    fun onMessageCreate(event: MessageCreateEvent) {}
    fun onBotReady(event: ReadyEvent) {}
    fun onGenericEvent(event: Event) {}

}