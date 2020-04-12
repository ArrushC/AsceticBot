package com.arrush.ascetic.commands.utility

import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.utility.editContent
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
class PingCommand : Command("ping", CommandCategory.UTILITY,"{prefix}ping") {

    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> =
            Translator.any(event.language(), "command.ping").translateMono().flatMap { event.replyAndGet(it) }.flatMap {
                it.editContent(Translator.any(event.language(), "command.ping.edit")
                        .translate((System.currentTimeMillis() - it.timestamp.toEpochMilli()), event.responseTime()))
            }.then()
}

