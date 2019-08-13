package com.arrush.ascetic.commands.developer

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import discord4j.core.`object`.util.Permission
import reactor.core.publisher.Mono
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class EvalCommand : Command("eval", CommandCategory.DEVELOPERS, "eval <code>", "Evaluates code", listOf(Permission.SEND_MESSAGES)) {

    override fun onCommand(event: CommandEvent): Mono<Void> {
        val engine : ScriptEngine = ScriptEngineManager().getEngineByName("kotlin")

        return Mono.just(engine.eval(event.args.joinToString(separator = " ").removeSurrounding("```")))
                .map { result -> event.replyEmbed(event.getEmbed(null, null, listOf(listOf("Your Code", "```kotlin\n${event.args.joinToString(separator = " ")}\n```", false), listOf("Your Result", "```fix\n$result\n```", false)))).subscribe() }.then()
    }


    override fun runCommand(event: CommandEvent) {
        if (event.hasArgs) super.runCommand(event)
    }
}