package com.arrush.ascetic.listeners

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import discord4j.core.`object`.entity.User
import discord4j.core.event.domain.guild.GuildCreateEvent
import discord4j.core.event.domain.lifecycle.ReadyEvent

class BotListener : IListener{
    override fun onBotReady(event: ReadyEvent) {
        Constants.getLogger().info("Attempting to save all absent guild data.")
        AsceticBot.INSTANCE.guildDb.saveMultiple(event.guilds.map { it.id }.toList())
        Constants.getLogger().success("Saved guild data. It is now up to date.")
        Constants.getLogger().success("Bot is ready!")
    }

    override fun onGuildCreate(event: GuildCreateEvent) {
        Constants.getLogger().info("Attempting to save all absent user data.")
        event.guild.members.filter { !it.isBot }.map(User::getId).collectList()
                .map {  AsceticBot.INSTANCE.userDb.saveMultiple(it) }.block()
        Constants.getLogger().success("Saved user data. It is now up to date.")
    }
}