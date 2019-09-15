package com.arrush.ascetic.commands.utility

import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
class PingCommand : Command("ping", CommandCategory.UTILITY,"{prefix}ping") {

    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> =
            Translator.any(event.language(), "command.ping").send(event.channel()).map {it.edit { spec -> spec.setContent(
                Translator.any(event.language(), "command.ping.edit")
                        .translate((System.currentTimeMillis() - it.timestamp.toEpochMilli()), event.responseTime())
            )}}.then()
}

