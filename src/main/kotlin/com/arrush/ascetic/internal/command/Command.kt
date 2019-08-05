package com.arrush.ascetic.internal.command

import discord4j.core.`object`.util.Permission
import reactor.core.publisher.Mono



@Suppress("MemberVisibilityCanBePrivate")
abstract class Command constructor(val name: String, val category: CommandCategory, val help: String = "No help provided.", val description: String = "No description provided.", val requiredPerms: List<Permission> = listOf(Permission.SEND_MESSAGES)) {

    abstract fun onCommand(event: CommandEvent): Mono<Void>

    open fun runCommand(event: CommandEvent) {

        event.guild.flatMap { Mono.zip(it.getMemberById(event.selfUserId), it.getMemberById(event.authorId)) }
                .flatMap { Mono.zip(it.t1.basePermissions, it.t2.basePermissions)}
                .map { it.t1.containsAll(this.requiredPerms) && it.t2.containsAll(this.requiredPerms)}
                .flatMap {
                    if (!it) event.replyAndGet("**Either you (${event.author.orElse(null).mention}) and/or I lack the permission to run this command.**").then() else  this.onCommand(event)
                }.subscribe()
    }
}