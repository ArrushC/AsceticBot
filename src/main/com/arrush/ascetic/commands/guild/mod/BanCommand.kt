package com.arrush.ascetic.commands.guild.mod

import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class BanCommand : Command("ban", CommandCategory.MODERATION, "ban <@mention> [reason]") {
    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val userId = event.message.userMentionIds.toMutableList()[0]

        return event.guild.flatMap { it.getMemberById(userId) } // getting user.
                .flatMap { // reasoning.
                    if (event.contentClean().isNotEmpty()) {
                        Translator.any(event.language(), "command.ban.reason.full").send(event.channel(), it.mention, event.contentClean())
                    } else Translator.any(event.language(), "command.ban.reason").send(event.channel(), it.mention)
                }.flatMap { event.guild } // getting guild.
                .flatMap { it.ban(userId) { spec -> spec.setDeleteMessageDays(1).reason = event.contentClean() } } // banning.
                .then()
    }

    override fun runCommand(event: MessageCreateEvent, vararg args: String) {
        if (event.hasArgs() && event.message.userMentionIds.isNotEmpty()) return
    }
}