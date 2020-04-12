package com.arrush.ascetic.commands.guild.settings

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import discord4j.core.`object`.util.Permission
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class LanguageCommand: Command("language", CommandCategory.SETTINGS, "language [en]", listOf(Permission.MANAGE_MESSAGES)) {

    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val data = AsceticBot.getGuildData(event.guildId())
        data.language = args[0]
        AsceticBot.updateGuildData(data)
        return Translator.any(event.language(), "command.language.changed").send(event.channel(), args[0]).then()
    }

    override fun runCommand(event: MessageCreateEvent, vararg args: String) {
        if (event.hasArgs()) {
            super.runCommand(event)
        } else {
            Translator.any(event.language(), "command.language.current").send(event.channel(), event.language()).subscribe()
        }
    }

}