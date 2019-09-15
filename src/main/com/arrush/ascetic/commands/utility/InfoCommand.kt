package com.arrush.ascetic.commands.utility

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.utility.EmbedUtils
import com.arrush.ascetic.utility.orderedReplace
import com.arrush.ascetic.utility.replaceFieldValue
import com.sun.management.OperatingSystemMXBean
import discord4j.common.GitProperties
import discord4j.core.event.domain.message.MessageCreateEvent
import org.apache.commons.io.FileUtils
import reactor.core.publisher.Mono
import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import java.util.*

@Suppress("UNCHECKED_CAST")
class InfoCommand : Command("info", CommandCategory.UTILITY, "{prefix}info") {

    @Suppress("UNUSED_VARIABLE")
    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val osManagement = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
        val cpu = osManagement.systemCpuLoad * 100
        val freeMemory = FileUtils.byteCountToDisplaySize(Runtime.getRuntime().freeMemory())
        val totalMemory = FileUtils.byteCountToDisplaySize(Runtime.getRuntime().totalMemory())
        val uptimeDiff = System.currentTimeMillis() - AsceticBot.INSTANCE.startTime
        val uptime: String = SimpleDateFormat("dd-HH-mm").format(Date(uptimeDiff))
                .orderedReplace("-", " days, ", " hours and ")+ " minutes" // need to do something with this to make it translatable.

        val botVersion = Translator.any(event.language(), "command.info.bot.embed.fields").array().array(0).toArray()
                .replaceFieldValue("{bot.version}", AsceticBot.INSTANCE.getVersion()).toList()
        val botCmds = Translator.any(event.language(), "command.info.bot.embed.fields").array().array(1).toArray()
                .replaceFieldValue("{size}", AsceticBot.INSTANCE.commandRegistry.commands.size.toString()).toList()
        val d4jVersion = Translator.any(event.language(), "command.info.bot.embed.fields").array().array(2).toArray()
                .replaceFieldValue("{d4j.version}", GitProperties.GIT_COMMIT_ID_DESCRIBE).toList()
        val cpuUsage = Translator.any(event.language(), "command.info.bot.embed.fields").array().array(3).toArray()
                .replaceFieldValue("{cpu}", cpu.toString()).replaceFieldValue("{ram}", freeMemory.toString()).toList()
        val memoryUsage = Translator.any(event.language(), "command.info.bot.embed.fields").array().array(4).toArray()
                .replaceFieldValue("{ram}", freeMemory.toString()).replaceFieldValue("{total}", totalMemory).toList()
        val threadCount = Translator.any(event.language(), "command.info.bot.embed.fields").array().array(5).toArray()
                .replaceFieldValue("{count}", Thread.activeCount().toString()).toList()
        val uptimeField = Translator.any(event.language(), "command.info.bot.embed.fields").array().array(6).toArray()
                .replaceFieldValue("{uptime}", uptime).toList()

        return event.replyEmbed(EmbedUtils.getEmbed(
                title = Translator.any(event.language(), "command.info.bot.embed.title").translate(),
                description = Translator.any(event.language(), "command.info.bot.embed.description").translate(),
                fields = listOf(botVersion, botCmds, d4jVersion, cpuUsage, memoryUsage, threadCount, uptimeField),
                footer = listOf(Translator.any(event.language(), "command.info.embed.footer").translate(event.authorNameFull()), null))).then()
    }



    override fun runCommand(event: MessageCreateEvent, vararg args: String) {
        if (!event.hasArgs()) super.runCommand(event, *args)
    }

}