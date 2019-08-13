package com.arrush.ascetic.commands.utility

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.utility.Container
import com.arrush.ascetic.utility.orderedReplace
import com.sun.management.OperatingSystemMXBean
import discord4j.common.GitProperties
import discord4j.core.`object`.entity.*
import discord4j.core.`object`.presence.Status
import org.apache.commons.io.FileUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.awt.Color
import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@Suppress("UNCHECKED_CAST")
class InfoCommand : Command("info", CommandCategory.UTILITY, "info [guild]", "Tells you information about me or this guild!") {

    override fun onCommand(event: CommandEvent): Mono<Void> {
        val osManagement = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
        val cpu = osManagement.systemCpuLoad * 100
        val freeMemory = FileUtils.byteCountToDisplaySize(Runtime.getRuntime().freeMemory())
        val totalMemory = FileUtils.byteCountToDisplaySize(Runtime.getRuntime().totalMemory())
        val uptimeDiff = System.currentTimeMillis() - AsceticBot.INSTANCE.startTime
        val uptime: String = SimpleDateFormat("dd-HH-mm").format(Date(uptimeDiff))
                .orderedReplace("-", " days, ", " hours and ")+ " minutes"

        return event.replyEmbed(event.getEmbed(
            "Information about Unique Bot",
            "Hello! Here below are some things that you might get to know about me!",
            listOf(
                    listOf("Bot Version", Constants.VERSION.getString(), true),
                    listOf("Bot Commands", AsceticBot.INSTANCE.commandManager.commands.size.toString(), true),
                    listOf("D4J Version", GitProperties.GIT_COMMIT_ID_DESCRIBE, true),
                    listOf("CPU Usage", "CPU: `$cpu%`\nRAM: `$freeMemory`", true),
                    listOf("Memory Usage", "`$freeMemory` / `$totalMemory`", true),
                    listOf("Thread Count", Thread.activeCount().toString(), true),
                    listOf("Uptime", uptime, true)
            ), footer = listOf("Requested by ${event.authorName}#${event.authorDiscriminator}", null))).then()
    }

    private fun guildInfo(event: CommandEvent): Mono<Void> =event.guild.map { Container(it.emojis, it.memberCount.asInt,  it.channels, it.afkChannel, it.members, it.afkTimeout) }.flatMap {container ->  event.channel.flatMap { chan -> chan.createEmbed { spec ->
        spec.setTitle("Information about this guild")
        (container.fifth as Flux<Member>).flatMap {it.presence.filter{ p -> p.status != Status.INVISIBLE && p.status != Status.OFFLINE }}.collectList().map { spec.addField("Member Count", "${it.size} / ${container.second as Int}", true) }.subscribe()
        (container.third as Flux<GuildChannel>).filter {it !is VoiceChannel && it !is Category}.map { it.name }.collectList().map { spec.addField("Text Channels", it.size.toString(), true) }.subscribe()
        container.third.filter {it is VoiceChannel && it !is Category}.map { it.name }.collectList().map { spec.addField("Voice Channels", it.size.toString(), true) }.subscribe()
        (container.fourth as Mono<VoiceChannel>).map { it.name }.map {spec.addField("AFK Channel and Timeout", "Timeout: `${container.sixth as Int / 60}` mins\nName: `$it`", true) }.subscribe()
        (container.first as Flux<GuildEmoji>).map {"${it.asFormat()} • **`<${it.id.asLong()}:${it.name}:>`**"}.collectList().map { spec.addField("Guild Emojis", it.joinToString("\n"), true) }.subscribe()
        spec.setColor(Constants.COLOUR.get() as Color)
        spec.setTimestamp(Instant.now())
        spec.setFooter("Requested by ${event.authorName}#${event.authorDiscriminator}", null)
    }}}.then()


    override fun runCommand(event: CommandEvent) {
        when {
            !event.hasArgs -> super.runCommand(event)
            event.args[0].toLowerCase() == "guild" -> guildInfo(event).subscribe()
            //event.args[0].toLowerCase() == "user" -> userInfo(event).subscribe()
        }
    }

}