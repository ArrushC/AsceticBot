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
            val data = guildDb.guilds[event.guildId.asLong()]!!
            val oldPrefix = data.prefix
            data.prefix = event.args[0]
            guildDb.modify(data)
            event.replyAndGet("**Your prefix has been successfully changed from `$oldPrefix` to `${event.args[0]}` !**").then()
        } else {
            val prefix = guildDb.guilds[event.guildId.asLong()]!!.prefix
            event.replyAndGet("**The prefix for this guild is currently `$prefix`**").then()
        }
    }

}