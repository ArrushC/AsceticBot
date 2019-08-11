package com.arrush.ascetic.listeners

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.internal.database.guild.GuildDatabase
import com.arrush.ascetic.utility.DiscordUtils
import com.arrush.ascetic.utility.getAndRemove
import com.arrush.ascetic.utility.substringIf
import discord4j.core.event.domain.message.MessageCreateEvent

class CommandListener: IListener {

    override fun onMessageCreate(event: MessageCreateEvent) {
        if (event.message.author.map { it.isBot }.orElse(false) ) return
        val content = event.message.content.orElse("")!!

        val prefix = GuildDatabase.INSTANCE.guilds[event.guildId.orElse(null).asLong()]?.prefix ?: Constants.MENTION.getString()

        val args: MutableList<String> = when {
            content.startsWith(prefix) -> content.substringIf(prefix.length) { content.contains(" ") }.split(" ").toMutableList()
            else -> return
        }
        val cmdEvent = CommandEvent(args, event)
        args.removeAll { it.isEmpty() }
        val commandName = args.getAndRemove(0)

        if (!AsceticBot.INSTANCE.commandManager.commands.containsKey(commandName)) return
        if (Constants.IS_LOCKDOWN.getBoolean() && !DiscordUtils.isDeveloper(cmdEvent.authorId)) return
        if (AsceticBot.INSTANCE.commandManager.commands[commandName]?.category == CommandCategory.DEVELOPERS && !DiscordUtils.isDeveloper(cmdEvent.authorId)) return

        AsceticBot.INSTANCE.commandManager.commands[commandName]!!.runCommand(cmdEvent)
    }
}