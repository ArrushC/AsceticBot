package com.arrush.ascetic.listeners

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import discord4j.core.event.domain.lifecycle.ReadyEvent

class BotListener : IListener{
    override fun onBotReady(event: ReadyEvent) {
        Constants.getLogger().info("Attempting to save all absent guild data.")
        AsceticBot.INSTANCE.guildDb.saveMultiple(event.guilds.toList())
        Constants.getLogger().success("Saved guild data. It is now up to date.")
        Constants.getLogger().info("Attempting to save all absent user data.")
        event.client.users.collectList().map {
            AsceticBot.INSTANCE.userDb.saveMultiple(it)
        }.subscribe()
        Constants.getLogger().success("Saved user data. It is now up to date.")
        Constants.getLogger().success("Bot is ready!")
    }
}