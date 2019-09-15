package com.arrush.ascetic.commands.`fun`

import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.utility.EmbedUtils
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import kotlin.random.Random

class EightBallCommand : Command("8ball", CommandCategory.FUN, "8ball <question>") {

    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val choices = Translator.any(event.language(), "command.8ball.choices").array().toArray()
        return event.replyEmbed(EmbedUtils.getEmbed(
                description = choices[Random.nextInt(0, choices.size - 1)] as String,
                title = Translator.any(event.language(), "command.8ball.embed.title").translate(event.authorName()),
                footer = listOf(event.args().joinToString(separator = " ").removeSurrounding(" "), null, null))
        ).then()
    }

    override fun runCommand(event: MessageCreateEvent, vararg args: String) { if (event.hasArgs()) super.runCommand(event) }
}