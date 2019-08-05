package com.arrush.menu

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.listeners.eschedule.ScheduleStatus
import com.arrush.ascetic.utility.emptySnowflake
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.reaction.ReactionEmoji
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.event.domain.message.ReactionAddEvent
import discord4j.core.spec.EmbedCreateSpec

class Paginator internal constructor(private val tag: String, private val pages: MutableList<(EmbedCreateSpec) -> Unit>) {
    private val rewindEmoji: ReactionEmoji = ReactionEmoji.unicode("\u23EA")
    private val backEmoji: ReactionEmoji = ReactionEmoji.unicode("\u25C0")
    private val stopEmoji: ReactionEmoji = ReactionEmoji.unicode("\u23F9")
    private val nextEmoji: ReactionEmoji = ReactionEmoji.unicode("\u25B6")
    private val forwardEmoji: ReactionEmoji = ReactionEmoji.unicode("\u23E9")

    private val firstPage = 0
    private val lastPage = pages.size - 1
    private var pageNo = 0
    private var messageId: Snowflake = emptySnowflake()

    fun startPagination(event: MessageCreateEvent) {
        messageId = setupMessage(event)
        paginate(event)
    }

    private fun paginate(e: MessageCreateEvent) {
        AsceticBot.instance.reactionScheduler.scheduleEvent(tag ,e.guildId.orElse(emptySnowflake())) {
            if ((it.messageId != this.messageId) || (it.userId != e.member.map { u -> u.id}.orElse(emptySnowflake())) || (e.message.channelId != it.channelId)) return@scheduleEvent ScheduleStatus(ScheduleStatus.Status.NOT_COMPLETED, 301)

            when (it.emoji) {
                this.backEmoji, this.rewindEmoji -> {
                    previousPage(it, rewind=(it.emoji == this.rewindEmoji))
                    return@scheduleEvent ScheduleStatus(ScheduleStatus.Status.NOT_COMPLETED, 301)
                }
                this.stopEmoji -> {
                    it.message.flatMap { msg -> msg.delete() }.subscribe()
                    return@scheduleEvent ScheduleStatus(ScheduleStatus.Status.COMPLETED, 400)
                }
                this.nextEmoji, this.forwardEmoji -> {
                    nextPage(it, fastForward = (it.emoji == this.forwardEmoji))
                    return@scheduleEvent ScheduleStatus(ScheduleStatus.Status.NOT_COMPLETED, 301)
                }
                else -> return@scheduleEvent ScheduleStatus(ScheduleStatus.Status.NOT_COMPLETED, 301)
            }
        }
    }

    private fun setupMessage(event: MessageCreateEvent): Snowflake {
        lateinit var message: Message
        event.message.channel.flatMap { it.createMessage { spec -> spec.setEmbed (pages[pageNo])} }.subscribe {message = it}

        message.addReaction(rewindEmoji).subscribe()
        message.addReaction(backEmoji).subscribe()
        message.addReaction(stopEmoji).subscribe()
        message.addReaction(nextEmoji).subscribe()
        message.addReaction(forwardEmoji).subscribe()
        return message.id
    }

    private fun editMessage(event: ReactionAddEvent) {
        event.message.flatMap { it.edit { spec -> spec.setEmbed (pages[pageNo]) } }
                .flatMap { it.removeReaction(event.emoji, event.userId) }.subscribe()
    }

    private fun nextPage(event: ReactionAddEvent, fastForward: Boolean = false) {
        pageNo = if (fastForward) {
            lastPage
        } else {
            if (pageNo < lastPage) pageNo + 1 else 0
        }
        this.editMessage(event)
    }

    private fun previousPage(event: ReactionAddEvent, rewind: Boolean = false) {
        pageNo = if (rewind) {
            firstPage
        } else {
            if (pageNo > firstPage) pageNo - 1 else lastPage
        }
        this.editMessage(event)
    }
}