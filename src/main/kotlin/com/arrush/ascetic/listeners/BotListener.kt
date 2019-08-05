package com.arrush.ascetic.listeners

import discord4j.core.event.domain.lifecycle.ReadyEvent

class BotListener : IListener{
    override fun onBotReady(event: ReadyEvent) {
        println("Bot is ready!")
    }
}