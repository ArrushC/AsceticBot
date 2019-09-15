package com.arrush.ascetic.commands.guild.mod

import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.utility.removeAllAndForm
import discord4j.core.`object`.util.Permission
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class KickCommand : Command("kick", CommandCategory.MODERATION, "kick <@mention> [reason]", listOf(Permission.KICK_MEMBERS)) {
    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val userId = event.message.userMentionIds.toMutableList()[0]
        val reason = event.args().removeAllAndForm { it.contains(Regex("(<(@|!@)([0-9]+)+>)+")) }.joinToString(separator = " ").removeSurrounding(" ")


        return event.guild.flatMap {it.getMemberById(userId) }
                .flatMap {
                    if (reason.isNotEmpty()) {
                        Translator.any(event.language(), "command.kick.reason.full").send(event.channel(), it.mention, reason).then()
                    } else {
                        Translator.any(event.language(), "command.kick.reason").send(event.channel(), it.mention).then()
                    }
                }.flatMap { event.guild }
                .flatMap { it.kick(userId, reason) }
                .then()
    }

    override fun runCommand(event: MessageCreateEvent, vararg args: String) {
        if (event.hasArgs() && event.message.userMentionIds.isNotEmpty()) super.runCommand(event)
    }
}