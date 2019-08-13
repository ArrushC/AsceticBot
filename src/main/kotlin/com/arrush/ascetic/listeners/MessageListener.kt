package com.arrush.ascetic.listeners

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.internal.cooldown.CooldownManager
import com.arrush.ascetic.utility.DiscordUtils
import com.arrush.ascetic.utility.getAndRemove
import com.arrush.ascetic.utility.substringIf
import discord4j.core.event.domain.message.MessageCreateEvent
import java.util.concurrent.TimeUnit

class MessageListener: IListener {

    override fun onMessageCreate(event: MessageCreateEvent) {
        if (event.message.author.map { it.isBot }.orElse(false) ) return
        println("thonk")
        this.execCommand(event)
        this.addExp(event)
    }

    private fun execCommand(event: MessageCreateEvent) {
        val content = event.message.content.orElse("")!!

        println("Okay")

        val prefix = if (content.startsWith(Constants.MENTION.getString())) {
            Constants.MENTION.getString()
        } else AsceticBot.INSTANCE.guildDb.guilds[event.guildId.orElse(null).asLong()]!!.prefix

        println("Good")

        if (!content.startsWith(prefix)) return

        println("better")

        val args: MutableList<String> = content.substringIf(prefix.length) { content.contains(" ") }.split(" ").toMutableList()
        val cmdEvent = CommandEvent(args, event)
        args.removeAll { it.isEmpty() }
        val commandName = args.getAndRemove(0)

        if (!AsceticBot.INSTANCE.commandManager.commands.containsKey(commandName)) return
        if (Constants.IS_LOCKDOWN.getBoolean() && !DiscordUtils.isDeveloper(cmdEvent.authorId)) return
        if (AsceticBot.INSTANCE.commandManager.commands[commandName]?.category == CommandCategory.DEVELOPERS && !DiscordUtils.isDeveloper(cmdEvent.authorId)) return

        println("Excellent")

        AsceticBot.INSTANCE.commandManager.commands[commandName]!!.runCommand(cmdEvent)

    }

    private fun addExp(event: MessageCreateEvent) {
        val userId = event.member.orElse(null).id
        val expData = AsceticBot.INSTANCE.userDb.users[userId.asLong()]!!.expData
        val key = "${userId.asString()}-exp"

        if (!CooldownManager.INSTANCE.isOnCooldown(key)) {
            expData.addExp(10)
            CooldownManager.INSTANCE.addCooldown(key, 2, TimeUnit.MINUTES)
        } else {
            println("Tried to gain exp but has ${CooldownManager.INSTANCE.getRemainingCooldown(key, TimeUnit.SECONDS)} seconds remaining.")
        }

        expData.levelUp {success ->
            event.message.channel
                    .flatMap { it.createMessage(":uo: **Congratulations! You just leveled up to lvl ${success.getLvl()}**") }
                    .subscribe()
        }
    }
}