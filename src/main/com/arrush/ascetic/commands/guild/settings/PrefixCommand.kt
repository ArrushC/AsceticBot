package com.arrush.ascetic.commands.guild.settings

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.utility.emptySnowflake
import discord4j.core.`object`.util.Permission
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class PrefixCommand : Command("prefix", CommandCategory.MODERATION, "prefix [new_prefix]",  listOf(Permission.MANAGE_MESSAGES)) {

    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val guildDb = AsceticBot.INSTANCE.guildDb
        return if (event.hasArgs()) {
            guildDb.guilds[event.guildId.orElse(emptySnowflake()).asLong()]!!.changePrefix(args[0])
            Translator.any(event.language(), "command.prefix.changed").send(event.channel(), args[0]).then()
        } else {
            Translator.any(event.language(), "command.prefix.current").send(event.channel(), event.prefix()).then()
        }
    }

}