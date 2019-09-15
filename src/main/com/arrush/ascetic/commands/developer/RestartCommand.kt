package com.arrush.ascetic.commands.developer

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.replyAndGet
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import kotlin.system.exitProcess

class RestartCommand : Command("restart", CommandCategory.DEVELOPERS, "restart [shard no]") {
    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        return event.replyAndGet("**Restarting bot!**")
                .then().map { exitProcess(0) }
    }


}