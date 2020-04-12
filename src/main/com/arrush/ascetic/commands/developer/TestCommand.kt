package com.arrush.ascetic.commands.developer

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.replyAndGet
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono


class TestCommand : Command("test", CommandCategory.DEVELOPERS,"{prefix}test") {
    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> = event.replyAndGet("Nothing to test for now...").then()
}