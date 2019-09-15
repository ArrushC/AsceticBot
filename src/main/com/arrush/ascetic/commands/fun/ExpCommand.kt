package com.arrush.ascetic.commands.`fun`

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.enums.Emojis
import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.utility.EmbedUtils
import com.arrush.ascetic.utility.replaceMultiple
import com.arrush.ascetic.utility.times
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import java.math.RoundingMode
import java.text.DecimalFormat

@Suppress("UNCHECKED_CAST")
class ExpCommand : Command("exp", CommandCategory.FUN, "{prefix}exp") {

    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val expData = AsceticBot.INSTANCE.getExpData(event.authorId())

        val totalExp = expData.getExp()
        val level = expData.getLvl()

        val expToNext = expData.expToNext()
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        val barsNeeded = (df.format(totalExp.toDouble() / expToNext.toDouble()).toDouble() * 10).toLong()
        val barsRemaining = 10 - barsNeeded

        val fieldOne = Translator.any(event.language(), "command.exp.embed.fields").array().array(0).toArray()
                .replaceMultiple(0, mapOf("{level}" to level.toString(), "{exp}" to totalExp.toString())).toList()
        val fieldTwo = Translator.any(event.language(), "command.exp.embed.fields").array().array(1).toArray()
                .replaceMultiple(1, mapOf(
                        "{level}" to (level +1).toString(),
                        "{exp}" to (expToNext - totalExp).toString(),
                        "{bar}" to (Emojis.XP_BAR.value * barsNeeded) + (" " * barsRemaining)
                )).toList()

        return event.replyEmbed(EmbedUtils.getEmbed(
                title= Translator.any(event.language(), "command.exp.embed.title").translate(),
                description = Translator.any(event.language(), "command.exp.embed.description").translate(),
                fields = listOf(fieldOne, fieldTwo)
        )).then()
    }
}

