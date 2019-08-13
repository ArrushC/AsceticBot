package com.arrush.ascetic.commands.developer

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import reactor.core.publisher.Mono
import kotlin.system.exitProcess

class RestartCommand : Command("restart", CommandCategory.DEVELOPERS, "restart [shard no]", "Restarts the bot or a shard.") {
    override fun onCommand(event: CommandEvent): Mono<Void> {
        return event.replyAndGet("**Restarting bot!**")
                .then().map { exitProcess(0) }
    }


}