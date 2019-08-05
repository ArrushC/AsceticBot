package com.arrush.ascetic.listeners

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.utility.DiscordUtils
import com.arrush.ascetic.utility.emptySnowflake
import com.arrush.ascetic.utility.getAndRemove
import com.arrush.ascetic.utility.substringIf
import discord4j.core.event.domain.message.MessageCreateEvent

class CommandListener: IListener {

    override fun onMessageCreate(event: MessageCreateEvent) {
        if (event.message.author.map { it.isBot }.orElse(false) ) return
        val content = event.message.content.orElse("")!!

        val args: MutableList<String> = when {
            content.startsWith(Constants.PREFIX.getString()) -> content.substringIf(Constants.PREFIX.getString().length) { content.contains(" ") }.split(" ").toMutableList()
            content.startsWith(Constants.MENTION.getString()) -> content.substringIf(Constants.MENTION.getString().length) { content.contains(" ") }.split(" ").toMutableList()
            else -> return
        }
        val cmdEvent = CommandEvent(args, event)
        args.removeAll { it.isEmpty() }
        val commandName = args.getAndRemove(0)

        if (!AsceticBot.instance.commandManager.commands.containsKey(commandName)) return
        if (Constants.IS_LOCKDOWN.getBoolean() && !DiscordUtils.isDeveloper(cmdEvent.authorId)) return
        if (AsceticBot.instance.commandManager.commands[commandName]?.category == CommandCategory.DEVELOPERS && !DiscordUtils.isDeveloper(cmdEvent.authorId)) return

        AsceticBot.instance.commandManager.commands[commandName]!!.runCommand(cmdEvent)
    }
}