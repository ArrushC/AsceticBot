package com.arrush.ascetic.commands.guild.mod

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.utility.removeAllAndForm
import discord4j.core.`object`.util.Permission
import reactor.core.publisher.Mono

class KickCommand : Command("kick", CommandCategory.MODERATION, "kick <@mention> [reason]", "Kicks someone from the user (with a reason).", listOf(Permission.KICK_MEMBERS)) {
    override fun onCommand(event: CommandEvent): Mono<Void> {
        val userId = event.message.userMentionIds.toMutableList()[0]
        val reason = event.args.removeAllAndForm { it.contains(Regex("(<(@|!@)([0-9]+)+>)+")) }.joinToString(separator = " ").removeSurrounding(" ")

        return event.guild.flatMap {it.getMemberById(userId) }
                .flatMap {  event.replyAndGet("**${it.mention} has been kicked" + (if (reason.isNotEmpty()) " for the reason:**\n*$reason*" else "**")) }
                .flatMap { event.guild }
                .flatMap { it.kick(userId, reason) }
                .then()
    }

    override fun runCommand(event: CommandEvent) {
        if (event.hasArgs && event.message.userMentionIds.isNotEmpty()) super.runCommand(event)
    }
}