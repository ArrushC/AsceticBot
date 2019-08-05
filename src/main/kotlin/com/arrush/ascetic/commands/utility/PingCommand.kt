package com.arrush.ascetic.commands.utility

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import reactor.core.publisher.Mono

class PingCommand : Command("ping", CommandCategory.UTILITY,"{prefix}ping", "Checks how good the connection is.") {

    override fun onCommand(event: CommandEvent): Mono<Void> = event.replyAndGet("Pong!")
            .flatMap {msg ->  msg.edit { it.setContent("Pong!\nTime taken: `${System.currentTimeMillis() - msg.timestamp.toEpochMilli()}` ms\nWebsocket: `${event.client.responseTime}` ms") }}
            .then()
}