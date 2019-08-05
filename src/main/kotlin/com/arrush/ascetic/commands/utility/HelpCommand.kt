package com.arrush.ascetic.commands.utility

import com.arrush.menu.PaginatorBuilder
import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import discord4j.core.spec.EmbedCreateSpec
import reactor.core.publisher.Mono
import java.awt.Color
import java.time.Instant
import kotlin.streams.toList

class HelpCommand : Command("help", CommandCategory.UTILITY, "{prefix}help", "Provides information about my commands.") {


    override fun onCommand(event: CommandEvent): Mono<Void> {
        val builder = PaginatorBuilder("helpCmd")

        for (index in 0 until CommandCategory.values().size) {
            val category = CommandCategory.values()[index]
            val categoryTemplate = {spec: EmbedCreateSpec ->
                spec.setColor(Color(255, 6 ,6))
                    .setTitle("Commands in ${category.type} Category")
                    .setDescription(category.description)
                    .setFooter("Page ${index+1} / ${CommandCategory.values().size}", null)
                    .setTimestamp(Instant.now())

                for ((name, command) in AsceticBot.instance.commandManager.commands.toList().stream().filter { it.second.category == category}.toList()) {
                    spec.addField("${name.capitalize()} Command", "**Description:** ${command.description}\n**Help:** ${command.help}", true)
                }
            }
            builder.addItem(categoryTemplate)
        }

        builder.build().startPagination(event.event)

        return Mono.empty()
    }

    private fun onSingleHelpCommand(event: CommandEvent) {
        val commands = AsceticBot.instance.commandManager.commands
        val commandName = event.args[0]
        if (!commands.containsKey(commandName)) {
            event.reply("`${commandName.capitalize()}` Command does not exist.\nPlease try again.")
            return // Will soon replace it with a search system.
        }


        val command = commands[commandName]!!

        event.author.orElse(null).privateChannel
                .flatMap {
                    it.createMessage("**Information about ${commandName.capitalize()} Command**\nDescription: **${command.description}**\nHelp: **${command.help}**")
                }.subscribe()
    }


    override fun runCommand(event: CommandEvent) {
        if (!event.hasArgs) super.runCommand(event) else onSingleHelpCommand(event)
    }

}