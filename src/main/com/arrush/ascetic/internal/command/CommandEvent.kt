@file:Suppress("unused")

package com.arrush.ascetic.internal.command




import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.database.guild.GuildData
import com.arrush.ascetic.internal.database.user.ExpData
import com.arrush.ascetic.internal.database.user.UserData
import com.arrush.ascetic.utility.emptySnowflake
import com.arrush.ascetic.utility.removeAllAndForm
import com.arrush.ascetic.utility.substringIf
import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.PrivateChannel
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.spec.EmbedCreateSpec
import reactor.core.publisher.Mono
import java.util.*


fun MessageCreateEvent.args(): MutableList<String> {
    val prefix = this.retrievePrefix()
    val args = this.content().substringIf(prefix.length) { this.content().contains(" ") }.split(" ").toMutableList()
    args.removeAll { it.isEmpty() }
    return args
}

fun MessageCreateEvent.guildId(): Snowflake = this.guildId.orElse(emptySnowflake())
fun MessageCreateEvent.guildData(): GuildData = AsceticBot.getGuildData(this.guildId())
fun MessageCreateEvent.language(): String = this.guildData().language
fun MessageCreateEvent.prefix(): String = this.guildData().prefix
fun MessageCreateEvent.retrievePrefix(): String = if (this.content().startsWith(AsceticBot.getMention())) {
    AsceticBot.getMention()
} else AsceticBot.guildDb.guilds[this.guildId.orElse(null).asLong()]?.prefix ?: AsceticBot.getPrefix()
fun MessageCreateEvent.userData(): UserData = AsceticBot.getUserData(this.authorId())
fun MessageCreateEvent.expData(): ExpData = AsceticBot.getExpData(this.authorId())
fun MessageCreateEvent.channel(): Mono<MessageChannel> = this.message.channel
fun MessageCreateEvent.content(): String = this.message.content.orElse("")
fun MessageCreateEvent.contentClean(): String = this.args().removeAllAndForm { it.contains(Regex("(<(@|!@)([0-9]+)+>)+")) }.joinToString(separator = " ").removeSurrounding(" ")
fun MessageCreateEvent.messageId(): Snowflake = this.message.id
fun MessageCreateEvent.author(): Optional<User> = this.message.author
fun MessageCreateEvent.authorMention(): String = this.author().map { it.mention }.orElse("")
fun MessageCreateEvent.authorChannel(): Mono<PrivateChannel> = this.author().map { it.privateChannel as Mono<PrivateChannel> }.orElse(null)
fun MessageCreateEvent.authorName(): String = this.author().map { it.username }.orElse("")
fun MessageCreateEvent.authorNameFull(): String = "${this.authorName()}#${this.authorDiscriminator()}"
fun MessageCreateEvent.authorId(): Snowflake = this.author().map { it.id }.orElse(emptySnowflake())
fun MessageCreateEvent.authorDiscriminator(): String = this.author().map { it.discriminator }.orElse("")
fun MessageCreateEvent.client(): DiscordClient = this.client
fun MessageCreateEvent.responseTime() = this.client.responseTime
fun MessageCreateEvent.selfUser(): Mono<User> = this.client.self
fun MessageCreateEvent.selfUserId(): Snowflake = this.client.selfId.orElse(emptySnowflake())
fun MessageCreateEvent.hasArgs(): Boolean = this.args().isNotEmpty()

fun MessageCreateEvent.replyAndGet(message: String): Mono<Message> = this.channel().flatMap { it.createMessage(message) }
fun MessageCreateEvent.reply(message: String) {this.replyAndGet(message).subscribe()}
fun MessageCreateEvent.replyEmbed(spec: (EmbedCreateSpec) -> Unit): Mono<Message> = this.channel().flatMap { it.createEmbed(spec) }

