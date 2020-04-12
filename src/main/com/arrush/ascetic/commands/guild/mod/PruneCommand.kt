package com.arrush.ascetic.commands.guild.mod

import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.utility.getPossibleInt
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class PruneCommand : Command("prune", CommandCategory.MODERATION,"prune [messageAmount]") {
    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        var messageAmount = event.args()[0].getPossibleInt()
        if (messageAmount == null) {
            return Translator.any(event.language(), "command.prune.notprovided").send(event.channel()).then()
        } else if (messageAmount > 100) {
            messageAmount = 100
        }

        return event.message.delete().then (
                event.channel()
                .flatMap { it.getMessagesBefore(event.messageId()).take(messageAmount.toLong()).collectList() }
                .map { it.forEach { msg -> msg.delete("Prune Command by ${event.authorName()}").subscribe() } }
                .flatMap { Translator.any(event.language(), "command.prune.pruned").send(event.channel(), messageAmount.toString()).then()} )

    }

    override fun runCommand(event: MessageCreateEvent, vararg args: String) {
        if (event.hasArgs()) super.runCommand(event)
    }
}