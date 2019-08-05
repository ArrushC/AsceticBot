package com.arrush.ascetic.commands.devs

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.utility.DiscordUtils
import com.arrush.ascetic.utility.emptySnowflake
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono


class TestCommand : Command("test", CommandCategory.DEVELOPERS,"{prefix}test", "For developers to test some features...") {
    override fun onCommand(event: CommandEvent): Mono<Void> {
        return event.channel.flatMap { it.createMessage("No testing for now.") }.then()
    }

    private fun sameAuthor(e1: CommandEvent, e2: MessageCreateEvent): Boolean = e1.authorId == e2.member.map { it.id }.orElse(emptySnowflake())
    private fun sameChans(e1: CommandEvent, e2: MessageCreateEvent): Boolean = Mono.zip(e1.channel, e2.message.channel).map { it.t1 == it.t2 }.block()!!

    // Successful ested code here.
    /*
    event.reply("What do you like ?")
    AsceticBot.instance!!.messageEventScheduler.scheduleEvent {
    if (it.member.map { user -> user.id }.orElse(emptySnowflake()) != event.authorId) return@scheduleEvent false

    event.reply("So you like ${it.message.content.orElse("")} ?")

    return@scheduleEvent true
    }*/
    /*
    event.reply("What do you like ?")
        AsceticBot.instance.messageScheduler.scheduleChainedEvents({ return@scheduleChainedEvents sameAuthor(event, it) && sameChans(event, it)}, {
            event.reply("So you like ${it.message.content.orElse("")} ?")
            return@scheduleChainedEvents true
        }, {
            event.reply("`${it.message.content.orElse("")}` wont be enough. Tell me more about it.")
            return@scheduleChainedEvents true
        }, {
            event.reply("That sounds interesting... nice talking with you btw, ${it.message.author.map { user -> user.mention }.orElse("")}")
            return@scheduleChainedEvents true
        }, {
            event.reply("Im always watching...")
            return@scheduleChainedEvents true
        })
     */
}