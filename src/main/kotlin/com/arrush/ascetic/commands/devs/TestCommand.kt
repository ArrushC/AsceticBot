package com.arrush.ascetic.commands.devs

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import reactor.core.publisher.Mono


class TestCommand : Command("test", CommandCategory.DEVELOPERS,"{prefix}test", "For developers to test some features...") {
    override fun onCommand(event: CommandEvent): Mono<Void> {
        return event.channel.flatMap { it.createMessage("No testing for now. Maybe test database :eyes:") }.then()
    }
}