package com.arrush.ascetic.internal.command

import com.arrush.ascetic.utility.formMap
import com.arrush.ascetic.utility.getMappedSubTypes
import org.reflections.Reflections

class CommandManager {
    var commands: MutableMap<String, Command> = mutableMapOf()

    init {
        commands = Reflections("com.arrush.unique.commands").getMappedSubTypes(Command::class.java).formMap({it.name }) {it}
    }

    fun registerCommand(command: Command) {
        commands[command.name] = command
    }
}