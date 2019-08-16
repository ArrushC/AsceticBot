package com.arrush.ascetic.listeners

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.utility.DiscordUtils
import com.arrush.ascetic.utility.getAndRemove
import com.arrush.ascetic.utility.substringIf
import discord4j.core.event.domain.message.MessageCreateEvent
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

class MessageListener: IListener {

    override fun onMessageCreate(event: MessageCreateEvent) {
        if (event.message.author.map { it.isBot }.orElse(false) ) return
        this.execCommand(event)
        this.addExp(event)
    }

    private fun execCommand(event: MessageCreateEvent) {
        val content = event.message.content.orElse("")!!
        val prefix = if (content.startsWith(Constants.MENTION.getString())) {
            Constants.MENTION.getString()
        } else AsceticBot.INSTANCE.guildDb.guilds[event.guildId.orElse(null).asLong()]!!.prefix

        println(prefix)

        if (!content.startsWith(prefix)) return

        val args: MutableList<String> = content.substringIf(prefix.length) { content.contains(" ") }.split(" ").toMutableList()
        val cmdEvent = CommandEvent(args, event)
        args.removeAll { it.isEmpty() }
        val commandName = args.getAndRemove(0)

        if (!AsceticBot.INSTANCE.commandManager.commands.containsKey(commandName)) return
        if (Constants.IS_LOCKDOWN.getBoolean() && !DiscordUtils.isDeveloper(cmdEvent.authorId)) return
        if (AsceticBot.INSTANCE.commandManager.commands[commandName]?.category == CommandCategory.DEVELOPERS && !DiscordUtils.isDeveloper(cmdEvent.authorId)) return

        println("Excellent")

        AsceticBot.INSTANCE.commandManager.commands[commandName]!!.runCommand(cmdEvent)
        println("Aftermath of $commandName Command")
    }

    private fun addExp(event: MessageCreateEvent) {
        val userId = Objects.requireNonNull(event.member.map { it.id }.get())
        val data = Objects.requireNonNull(AsceticBot.INSTANCE.userDb.users[userId.asLong()]!!) // why does this not load lmao.
        val key = "${userId.asString()}-exp"

        if (!AsceticBot.INSTANCE.cooldownApplier.isOnCooldown(key)) {
            data.exp += 10
            AsceticBot.INSTANCE.cooldownApplier.applyCooldown(key, 2, ChronoUnit.MINUTES)
        } else {
            println("Tried to gain exp but has ${AsceticBot.INSTANCE.cooldownApplier.remainingCooldownFor(key, TimeUnit.MINUTES)} minutes remaining.")
        }

        val expToNext = (data.lvl + 1) * 100
        if ((data.exp / expToNext) == 1L) { // user has arrived to a new level.
            data.lvl += 1
            event.message.channel
                    .flatMap { it.createMessage(":up: **Congratulations! You just leveled up to lvl ${data.lvl}**") }
                    .subscribe()
        }

        AsceticBot.INSTANCE.userDb.modify(data)
    }
}