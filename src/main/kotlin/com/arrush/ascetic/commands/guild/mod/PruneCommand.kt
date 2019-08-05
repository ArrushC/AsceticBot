package com.arrush.ascetic.commands.guild.mod

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.utility.getPossibleInt
import reactor.core.publisher.Mono

class PruneCommand : Command("prune", CommandCategory.MODERATION,"prune [messageAmount]", "Deletes messages from a channel. (Maximum is 100)") {
    override fun onCommand(event: CommandEvent): Mono<Void> {
        var messageAmount = event.args[0].getPossibleInt()
        if (messageAmount == null) {
            return event.replyAndGet("You must provide a number of messages to delete!").then()
        } else if (messageAmount > 100) {
            messageAmount = 100
        }

        return event.message.delete().then (
                event.channel
                .flatMap { it.getMessagesBefore(event.messageId).take(messageAmount.toLong()).collectList() }
                .map { it.forEach { msg -> msg.delete("Prune Command by ${event.authorName}").subscribe() } }
                .flatMap { event.replyAndGet(":wastebasket: `$messageAmount` messages have been successfully recycled! :wastebasket:").then() } )

    }

    override fun runCommand(event: CommandEvent) {
        if (event.hasArgs) super.runCommand(event)
    }
}