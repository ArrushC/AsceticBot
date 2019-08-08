package com.arrush.ascetic.commands.guild.mod

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import discord4j.core.`object`.util.Permission
import reactor.core.publisher.Mono

class PrefixCommand : Command("prefix", CommandCategory.MODERATION, "prefix [new_prefix]", "Allows you to change or view your current prefix for this guild.", listOf(Permission.MANAGE_MESSAGES)) {
    // Override it for arguments: If args then change prefix else view prefix.
    override fun onCommand(event: CommandEvent): Mono<Void> {
        lateinit var mono: Mono<Void>

        // return if () ... else ...

        // Maybe return a mono that sends a message of either error or success.
        return Mono.empty()
    }

}