package com.arrush.ascetic.internal.command

import com.arrush.ascetic.commands.`fun`.EightBallCommand
import com.arrush.ascetic.commands.`fun`.ExpCommand
import com.arrush.ascetic.commands.`fun`.TalkShortCommand
import com.arrush.ascetic.commands.developer.DbExecuteCommand
import com.arrush.ascetic.commands.developer.EvalCommand
import com.arrush.ascetic.commands.developer.RestartCommand
import com.arrush.ascetic.commands.developer.TestCommand
import com.arrush.ascetic.commands.guild.mod.BanCommand
import com.arrush.ascetic.commands.guild.mod.KickCommand
import com.arrush.ascetic.commands.guild.mod.PruneCommand
import com.arrush.ascetic.commands.guild.settings.LanguageCommand
import com.arrush.ascetic.commands.guild.settings.PrefixCommand
import com.arrush.ascetic.commands.utility.HelpCommand
import com.arrush.ascetic.commands.utility.InfoCommand
import com.arrush.ascetic.commands.utility.PingCommand

class CommandRegistry {
    var commands: MutableMap<String, Command> = mutableMapOf()

    init {
        this.registerCommands(
                // developer commands
                DbExecuteCommand(),
                EvalCommand(),
                RestartCommand(),
                TestCommand(),
                // fun commands
                EightBallCommand(),
                ExpCommand(),
                TalkShortCommand(),
                // moderation commands
                BanCommand(),
                KickCommand(),
                PruneCommand(),
                // setting commands
                LanguageCommand(),
                PrefixCommand(),
                // utility commands
                HelpCommand(),
                InfoCommand(),
                PingCommand()
        )
    }

    fun registerCommands(vararg commands: Command): CommandRegistry {
        commands.forEach { this.registerCommand(it) }
        return this
    }

    fun registerCommand(command: Command): CommandRegistry {
        commands[command.name] = command
        return this
    }
}