package com.arrush.ascetic.listeners

import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.database.guild.GuildDatabase
import discord4j.core.event.domain.lifecycle.ReadyEvent

class BotListener : IListener{
    override fun onBotReady(event: ReadyEvent) {
        Constants.getLogger().info("Attempting to save all absent guild data.")
        GuildDatabase.INSTANCE.saveMultiple(event.guilds.toList())
        Constants.getLogger().success("Saved guild data. It is now up to date.")
        Constants.getLogger().info("Attempting to save all absent user data.")
        event.client.users.collectList().flatMap {users ->

        }
        Constants.getLogger().success("Saved user data. It is now up to date.")
        Constants.getLogger().success("Bot is ready!")
    }
}