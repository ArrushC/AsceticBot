package com.arrush.ascetic

import com.arrush.ascetic.internal.RestartThread
import com.arrush.ascetic.internal.command.CommandRegistry
import com.arrush.ascetic.internal.database.bot.BotConfig
import com.arrush.ascetic.internal.database.guild.GuildData
import com.arrush.ascetic.internal.database.guild.GuildDatabase
import com.arrush.ascetic.internal.database.user.ExpData
import com.arrush.ascetic.internal.database.user.UserData
import com.arrush.ascetic.internal.database.user.UserDatabase
import com.arrush.ascetic.listeners.ListenerManager
import com.arrush.ascetic.listeners.eschedule.EventScheduler
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.presence.Activity
import discord4j.core.`object`.presence.Presence
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.event.domain.message.ReactionAddEvent
import discord4j.store.jdk.JdkStoreService
import me.arrush.javabase.databases.Database
import kotlin.system.measureTimeMillis




enum class AsceticBot {

    INSTANCE;

    val listenerManager: ListenerManager = ListenerManager()
    val commandRegistry: CommandRegistry = CommandRegistry()
    val messageScheduler: EventScheduler<MessageCreateEvent> = EventScheduler(MessageCreateEvent::class.java)
    val reactionScheduler: EventScheduler<ReactionAddEvent> = EventScheduler(ReactionAddEvent::class.java)
    val startTime: Long = System.currentTimeMillis()
    val database: Database = Database.withPostgres(5432, "postgres", "Arrush", "Password")!!
    val guildDb: GuildDatabase = GuildDatabase(this.database)
    val userDb: UserDatabase = UserDatabase(this.database)
    lateinit var botConfig: BotConfig

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            INSTANCE.start()
        }
    }

    private fun start() {
        val timeTaken = measureTimeMillis {
            this.loadDatabases()
            this.initThreads()
        }
        Constants.getLogger().info("I took $timeTaken ms to get ready for D4J initialisation!")
        this.initD4J(this.botConfig.token)
    }

    private fun loadDatabases() {
        Constants.getLogger().info("Attempting to load Bot Config from file [config.json].")
        this.botConfig = BotConfig.from()
        Constants.getLogger().success("Bot Config successfully loaded")
        Constants.getLogger().info("Attempting to load guild data from database.")
        this.guildDb.loadData()
        Constants.getLogger().success("All Guild data is successfully loaded.")
        Constants.getLogger().info("Attempting to load user data from database.")
        this.userDb.loadData()
        Constants.getLogger().success("All User data is successfully loaded.")
    }

    // some crap method. will remove it soon.
    private fun initThreads() {
        Runtime.getRuntime().addShutdownHook(RestartThread("Restart-Hook"))
        Constants.getLogger().info("Added a shutdown hook so I will revive at the point I die.")
    }

    private fun initD4J(token: String) {
        val client = DiscordClientBuilder(token)
                .setInitialPresence(Presence.doNotDisturb(Activity.listening("@Ascetic Bot help")))
                .setStoreService(JdkStoreService())
                .build()

        client.eventDispatcher.on(Event::class.java).subscribe { this.listenerManager.fireListeners(it) }

        client.eventDispatcher.on(MessageCreateEvent::class.java).subscribe { this.messageScheduler.onGenericEvent(it) }
        client.eventDispatcher.on(ReactionAddEvent::class.java).subscribe { this.reactionScheduler.onGenericEvent(it) }

        client.login().block()
    }

    fun getToken() = this.botConfig.token
    fun getVersion() = this.botConfig.version
    fun getMention() = this.botConfig.mention
    fun isLockdown() = this.botConfig.isLockdown
    fun getPrefix() = this.botConfig.prefix

    fun getGuildData(id: Snowflake): GuildData = this.guildDb.guilds[id.asLong()]!!
    fun getLanguage(id: Snowflake): String = this.getGuildData(id).language
    fun getUserData(id: Snowflake): UserData = this.userDb.users[id.asLong()]!!
    fun getExpData(id: Snowflake): ExpData = this.getUserData(id).expData

    fun updateGuildData(data: GuildData) = this.guildDb.modify(data)
    fun updateUserData(data: UserData) = this.userDb.modify(data)

    // plan to add abstract data.
}