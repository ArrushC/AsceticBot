package com.arrush.ascetic.commands.`fun`

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.listeners.eschedule.ScheduleStatus
import com.arrush.ascetic.utility.emptySnowflake
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class TalkShortCommand : Command("talkshort", CommandCategory.FUN,"{prefix}talkshort") {
    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val talks = Translator.any(event.language(), "command.talkshort.talks").array()
        event.reply(talks.string(0))
        AsceticBot.INSTANCE.messageScheduler.scheduleChainedEvents({
            return@scheduleChainedEvents ScheduleStatus.fromLogic(sameAuthor(event, it) && sameChans(event, it))
        }, {
            event.reply(talks.string(1).replace("{content}", it.message.content.orElse("nothing")))
            return@scheduleChainedEvents ScheduleStatus.Status.COMPLETED.get()
        }, {
            event.reply(talks.string(2))
            return@scheduleChainedEvents ScheduleStatus.Status.COMPLETED.get()
        }, {
            event.reply(talks.string(3).replace("{mention}", it.message.author.map {u -> u.mention }.orElse("")))
            return@scheduleChainedEvents ScheduleStatus.Status.COMPLETED.get()
        }, {
            event.reply(talks.string(4))
            return@scheduleChainedEvents ScheduleStatus.Status.COMPLETED.get()
        })

        return Mono.empty()
    }

    private fun sameAuthor(e1: MessageCreateEvent, e2: MessageCreateEvent): Boolean = e1.authorId() == e2.member.map { it.id }.orElse(emptySnowflake())
    private fun sameChans(e1: MessageCreateEvent, e2: MessageCreateEvent): Boolean = Mono.zip(e1.channel(), e2.message.channel).map { it.t1 == it.t2 }.block()!!
}