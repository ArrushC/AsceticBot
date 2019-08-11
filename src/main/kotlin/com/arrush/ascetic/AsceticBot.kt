package com.arrush.ascetic

import com.arrush.ascetic.internal.command.CommandManager
import com.arrush.ascetic.internal.database.guild.GuildDatabase
import com.arrush.ascetic.internal.threadding.RestartThread
import com.arrush.ascetic.listeners.ListenerManager
import com.arrush.ascetic.listeners.eschedule.EventScheduler
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.presence.Activity
import discord4j.core.`object`.presence.Presence
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.event.domain.message.ReactionAddEvent
import me.arrush.javabase.databases.Database


enum class AsceticBot {

    INSTANCE;

    val listenerManager: ListenerManager = ListenerManager()
    val commandManager: CommandManager = CommandManager()
    val messageScheduler: EventScheduler<MessageCreateEvent> = EventScheduler(MessageCreateEvent::class.java)
    val reactionScheduler: EventScheduler<ReactionAddEvent> = EventScheduler(ReactionAddEvent::class.java)
    val startTime: Long = System.currentTimeMillis()
    val database: Database = Database.withPostgres(5430, "postgres", "Arrush", "Password")!!
    private lateinit var token: String

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            AsceticBot.INSTANCE.start(args[0])
        }
    }

    private fun start(token: String) {
        this.token = token
        this.loadDatabases()
        this.initThreads()
        this.initD4J(token)
    }


    private fun loadDatabases() {
        GuildDatabase.INSTANCE.loadData()
    }

    // some crap method. will remove it soon.
    private fun initThreads() {
        Runtime.getRuntime().addShutdownHook(RestartThread("Restart-Hook"))
    }

    private fun initD4J(token: String) {
        val client = DiscordClientBuilder(token)
                .setInitialPresence(Presence.doNotDisturb(Activity.listening("@Ascetic Bot help")))
                .build()

        client.eventDispatcher.on(Event::class.java).subscribe { this.listenerManager.fireListeners(it) }
        client.eventDispatcher.on(MessageCreateEvent::class.java).subscribe { this.messageScheduler.onGenericEvent(it) }
        client.eventDispatcher.on(ReactionAddEvent::class.java).subscribe { this.reactionScheduler.onGenericEvent(it) }

        client.login().block()
    }

    fun getToken() = this.token
}