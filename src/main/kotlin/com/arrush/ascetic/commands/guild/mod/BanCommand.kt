package com.arrush.ascetic.commands.guild.mod

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import reactor.core.publisher.Mono

class BanCommand : Command("ban", CommandCategory.MODERATION, "ban <@mention> [reason]", "Bans someone from the server (comes with a reason too).") {
    override fun onCommand(event: CommandEvent): Mono<Void> {
        val userId = event.message.userMentionIds.toMutableList()[0]

        return event.guild.flatMap {
            event.replyAndGet("**${it.getMemberById(userId)}} has been banned" + (if (event.contentClean.isNotEmpty()) " for the reason:**\n${event.contentClean}" else "**"))
            it.ban(userId) { spec -> spec.setDeleteMessageDays(1).reason = event.contentClean } }.then()
    }

    override fun runCommand(event: CommandEvent) {
        if (event.hasArgs && event.message.userMentionIds.isNotEmpty()) return
    }
}