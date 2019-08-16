package com.arrush.ascetic.commands.guild.mod

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import discord4j.core.`object`.util.Permission
import reactor.core.publisher.Mono

class PrefixCommand : Command("prefix", CommandCategory.MODERATION, "prefix [new_prefix]", "Allows you to change or view your current prefix for this guild.", listOf(Permission.MANAGE_MESSAGES)) {
    override fun onCommand(event: CommandEvent): Mono<Void> {
        val guildDb = AsceticBot.INSTANCE.guildDb
        return if (event.hasArgs) {
            guildDb.guilds[event.guildId.asLong()]!!.changePrefix(event.args[0])
            event.replyAndGet("**Your prefix has been successfully changed to `${event.args[0]}` !**").then()
        } else {
            event.replyAndGet("**The prefix for this guild is currently `${guildDb.guilds[event.guildId.asLong()]!!.prefix}`**").then()
        }
    }

}