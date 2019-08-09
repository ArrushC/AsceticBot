package com.arrush.ascetic.listeners

import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.database.guild.GuildDatabase
import discord4j.core.event.domain.lifecycle.ReadyEvent

class BotListener : IListener{
    override fun onBotReady(event: ReadyEvent) {
        Constants.getLogger().info("Attempting to save all absent guild data.")
        GuildDatabase.INSTANCE.saveGuilds(event.guilds)
        Constants.getLogger().success("Saved guild data.")
        Constants.getLogger().success("Bot is ready!")
    }
}