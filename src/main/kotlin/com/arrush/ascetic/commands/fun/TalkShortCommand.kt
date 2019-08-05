package com.arrush.ascetic.commands.`fun`

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.listeners.eschedule.ScheduleStatus
import com.arrush.ascetic.utility.emptySnowflake
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class TalkShortCommand : Command("talkshort", CommandCategory.FUN,"{prefix}talkshort", "Fun way of talking to me.") {
    override fun onCommand(event: CommandEvent): Mono<Void> {
        event.reply("What do you like ?")
        AsceticBot.INSTANCE.messageScheduler.scheduleChainedEvents({
            return@scheduleChainedEvents ScheduleStatus.fromLogic(sameAuthor(event, it) && sameChans(event, it))
        }, {
            event.reply("So you like ${it.message.content.orElse("")} ?")
            return@scheduleChainedEvents ScheduleStatus.Status.COMPLETED.get()
        }, {
            event.reply("`${it.message.content.orElse("")}` wont be enough. Tell me more about it.")
            return@scheduleChainedEvents ScheduleStatus.Status.COMPLETED.get()
        }, {
            event.reply("That sounds interesting... nice talking with you btw, ${it.message.author.map { user -> user.mention }.orElse("")}")
            return@scheduleChainedEvents ScheduleStatus.Status.COMPLETED.get()
        }, {
            event.reply("Im always watching...")
            return@scheduleChainedEvents ScheduleStatus.Status.COMPLETED.get()
        })

        return Mono.empty()
    }

    private fun sameAuthor(e1: CommandEvent, e2: MessageCreateEvent): Boolean = e1.authorId == e2.member.map { it.id }.orElse(emptySnowflake())
    private fun sameChans(e1: CommandEvent, e2: MessageCreateEvent): Boolean = Mono.zip(e1.channel, e2.message.channel).map { it.t1 == it.t2 }.block()!!
}