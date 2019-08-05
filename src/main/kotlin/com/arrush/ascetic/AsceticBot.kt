package com.arrush.ascetic

import com.arrush.logger.Logger
import com.arrush.ascetic.internal.command.CommandManager
import com.arrush.ascetic.listeners.ListenerManager
import com.arrush.ascetic.listeners.eschedule.EventScheduler
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.presence.Activity
import discord4j.core.`object`.presence.Presence
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.event.domain.message.ReactionAddEvent
import java.awt.Color


@Suppress("MemberVisibilityCanBePrivate")
class AsceticBot(val token: String) {
    val listenerManager: ListenerManager = ListenerManager()
    val commandManager: CommandManager = CommandManager()
    val messageScheduler: EventScheduler<MessageCreateEvent> = EventScheduler(MessageCreateEvent::class.java)
    val reactionScheduler: EventScheduler<ReactionAddEvent> = EventScheduler(ReactionAddEvent::class.java)
    val startTime: Long = System.currentTimeMillis()
    //val lpManager: LavaPlayerManager = LavaPlayerManager()

    companion object {
        lateinit var instance: AsceticBot

        @JvmStatic
        fun main(args: Array<String>) {
            instance = AsceticBot(args[0])
            instance.start()
        }
    }

    private fun start() {
        this.initD4J()
    }

    private fun initD4J() {
        val client = DiscordClientBuilder(this.token)
                .setInitialPresence(Presence.doNotDisturb(Activity.listening("@Unique Bot help")))
                .build()
        client.eventDispatcher.on(Event::class.java).subscribe { this.listenerManager.fireListeners(it) }
        client.eventDispatcher.on(MessageCreateEvent::class.java).subscribe { this.messageScheduler.onGenericEvent(it) }
        client.eventDispatcher.on(ReactionAddEvent::class.java).subscribe { this.reactionScheduler.onGenericEvent(it) }

        client.login().block()
    }

}