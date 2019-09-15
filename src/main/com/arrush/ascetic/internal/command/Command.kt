package com.arrush.ascetic.internal.command

import com.arrush.ascetic.internal.languages.Translator
import discord4j.core.`object`.util.Permission
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono



@Suppress("MemberVisibilityCanBePrivate")
abstract class Command constructor(val name: String, val category: CommandCategory, val help:String, val requiredPerms: List<Permission> = listOf(Permission.SEND_MESSAGES)) {

    abstract fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void>

    open fun runCommand(event: MessageCreateEvent, vararg  args: String) {
        event.guild.flatMap { Mono.zip(it.getMemberById(event.selfUserId()), it.getMemberById(event.authorId())) }
                .flatMap { Mono.zip(it.t1.basePermissions, it.t2.basePermissions)}
                .map { it.t1.containsAll(this.requiredPerms) && it.t2.containsAll(this.requiredPerms)}
                .flatMap {
                    if (!it) {
                        Translator.any(event.language(), "command.permission.lack").sendDM(event.authorChannel(),
                                event.author().map { a -> a.mention }.orElse(""),
                                this.requiredPerms.joinToString { s -> s.name }
                        ).then()
                    } else  this.onCommand(event, *args)
                }.subscribe()
    }

    fun getDescription(language: String): String = Translator.any(language, "command.${this.name}.description").translate()

}