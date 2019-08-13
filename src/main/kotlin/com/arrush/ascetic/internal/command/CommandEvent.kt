package com.arrush.ascetic.internal.command

import com.arrush.ascetic.Constants
import com.arrush.ascetic.utility.emptySnowflake
import com.arrush.ascetic.utility.removeAllAndForm
import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.*
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.spec.EmbedCreateSpec
import reactor.core.publisher.Mono
import java.awt.Color
import java.time.Instant
import java.util.*

class CommandEvent(val args: MutableList<String>, val event: MessageCreateEvent) {
    val guild : Mono<Guild> get() = this.event.guild
    val guildId: Snowflake get() = this.event.guildId.orElse(emptySnowflake())
    val channel: Mono<MessageChannel> get() = this.event.message.channel
    val content: String get() = this.message.content.orElse("")
    val contentClean: String get() =  args.removeAllAndForm { it.contains(Regex("(<(@|!@)([0-9]+)+>)+")) }.joinToString(separator = " ").removeSurrounding(" ")
    val message: Message get() = this.event.message
    val messageId: Snowflake get() = this.message.id
    val member: Optional<Member> get() = this.event.member
    val author: Optional<User> get() = this.event.message.author
    val authorName: String get() = this.author.map {it.username}.orElse("")
    val authorId: Snowflake get() = this.author.map {it.id}.orElse(Snowflake.of(0))
    val authorDiscriminator: String get() = this.author.map { it.discriminator }.orElse("")
    val client: DiscordClient get() = this.event.client
    val selfUser: Mono<User> get() = this.client.self
    val selfUserId: Snowflake get() = this.client.selfId.orElse(emptySnowflake())
    val hasArgs: Boolean get() = this.args.isNotEmpty()

    fun replyAndGet(message: String): Mono<Message> = this.channel.flatMap { it.createMessage(message) }
    fun reply(message: String) {this.replyAndGet(message).subscribe()}
    fun replyEmbed(spec: (EmbedCreateSpec) -> Unit): Mono<Message> = this.channel.flatMap { it.createEmbed(spec) }

    fun getEmbed(title: String="", description: String="", author: List<String> = emptyList(), url: String ="", timestamp: Instant = Instant.now(), footer: List<String?> = emptyList(), imageUrl: String= "", thumbnailUrl: String="",  fields: List<List<Any>> = emptyList()): (EmbedCreateSpec) -> Unit = {
        it.setColor(Constants.COLOUR.get() as Color)
        it.setTimestamp(timestamp)

        if (title.isNotEmpty()) it.setTitle(title)
        if (description.isNotEmpty()) it.setDescription(description)
        if (author.isNotEmpty()) it.setAuthor(author[0], author[1], author[2])
        if (url.isNotEmpty()) it.setUrl(url)
        if (footer.isNotEmpty()) it.setFooter(footer[0]!!, footer[1])
        if (imageUrl.isNotEmpty()) it.setImage(imageUrl)
        if (thumbnailUrl.isNotEmpty()) it.setThumbnail(thumbnailUrl)
        if (fields.isNotEmpty()) {
            for (field in fields) {
                it.addField(field[0].toString(), field[1].toString(), field[2].toString().toBoolean())
            }
        }
    }
    fun getEmbed(title: String?, description: String?, fields: List<List<Any>>, footer: List<String?> = emptyList()): (EmbedCreateSpec) -> Unit = {
        it.setColor(Constants.COLOUR.get() as Color)
        it.setTimestamp(Instant.now())

        if (!title.isNullOrEmpty()) it.setTitle(title)
        if (!description.isNullOrEmpty()) it.setDescription(description)

        for (field in fields) {
            it.addField(field[0].toString(), field[1].toString(), field[2].toString().toBoolean())
        }

        if (footer.isNotEmpty()) it.setFooter(footer[0]!!, footer[1])
    }
}