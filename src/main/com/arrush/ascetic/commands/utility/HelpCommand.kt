package com.arrush.ascetic.commands.utility

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.internal.menu.PaginatorBuilder
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.spec.EmbedCreateSpec
import reactor.core.publisher.Mono
import java.awt.Color
import java.time.Instant
import kotlin.streams.toList

class HelpCommand : Command("help", CommandCategory.UTILITY, "{prefix}help") {


    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val builder = PaginatorBuilder("helpCmd")
        for (index in CommandCategory.values().indices) {
            val category = CommandCategory.values()[index]

            // "command.help.embed.field.value": "**Description:** {description}\n**Help:** {help}",
            val categoryTemplate = {spec: EmbedCreateSpec ->
                spec.setColor(Color(255, 6 ,6))
                    .setTitle("${category.type} Category")
                    .setDescription(this.getCategoryDescription(event.language(), category))
                    .setFooter("Page ${index+1} / ${CommandCategory.values().size}", null)
                    .setTimestamp(Instant.now())

                for ((name, command) in AsceticBot.commandRegistry.commands.toList().stream().filter { it.second.category == category}.toList()) {
                    spec.addField("${name.capitalize()} Command", Translator.any(event.language(), "command.help.embed.field.value").translate(command.getDescription(event.language()), command.help), true)
                }
            }
            builder.addItem(categoryTemplate)
        }

        builder.build().startPagination(event)

        return Mono.empty()
    }

    private fun onSingleHelpCommand(event: MessageCreateEvent) {
        val commands = AsceticBot.commandRegistry.commands
        val commandName = event.args()[0]
        if (!commands.containsKey(commandName)) {
            Translator.any(event.language(), "command.help.search.notexist").send(event.channel(), commandName.capitalize()).subscribe()
            return
        }
        val command = commands[commandName]!!
        Translator.any(event.language(), "command.help.search.command.single").sendDM(event.authorChannel(),
                commandName.capitalize(), command.getDescription(event.language()), this.formatHelp(command, event.prefix())).subscribe()
    }
    
    private fun getCategoryDescription(language: String, category: CommandCategory) = Translator.any(language, "category.${category.type.toLowerCase()}.description").translate()

    private fun formatHelp(command: Command, prefix: String) = command.help
            .replace("{prefix}", prefix)


    override fun runCommand(event: MessageCreateEvent, vararg args: String) {
        if (!event.hasArgs()) super.runCommand(event) else onSingleHelpCommand(event)
    }

}