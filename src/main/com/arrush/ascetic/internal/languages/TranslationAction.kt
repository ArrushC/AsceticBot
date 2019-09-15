package com.arrush.ascetic.internal.languages

import de.arraying.kotys.JSONArray
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.PrivateChannel
import discord4j.core.spec.EmbedCreateSpec
import reactor.core.publisher.Mono

class TranslationAction internal constructor(private val lang: Language, private val key: String) {
    // Methods for acquiring mainly the translated phrase of the message.
    fun translate(vararg args: Any) = this.lang.messagef(this.key, *args)
    fun array(): JSONArray = this.lang.messageArray(this.key)
    fun arrayString(index: Int): String = this.array().string(index)

    // Methods for sending them to channels.
    fun send(chan: Mono<MessageChannel>, vararg args: Any): Mono<Message> = chan.flatMap { it.createMessage(this.translate(*args))}
    fun sendDM(chan: Mono<PrivateChannel>, vararg args: Any): Mono<Message> = chan.flatMap { it.createMessage(this.translate(*args)) }

    // make one for emebed.
    fun embedFieldDesc(spec: EmbedCreateSpec, title: String, inline: Boolean=false, vararg args: String): EmbedCreateSpec = spec.addField(title, this.translate(*args), inline)
    fun embedFieldTitle(spec: EmbedCreateSpec, description: String, inline: Boolean=false, vararg args: String): EmbedCreateSpec = spec.addField(this.translate(*args), description, inline)
}