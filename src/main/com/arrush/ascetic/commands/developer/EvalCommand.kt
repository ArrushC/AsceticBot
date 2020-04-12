package com.arrush.ascetic.commands.developer

import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.utility.EmbedUtils
import discord4j.core.`object`.util.Permission
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class EvalCommand : Command("eval", CommandCategory.DEVELOPERS, "{prefix}eval <code>",  listOf(Permission.SEND_MESSAGES)) {

    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val engine : ScriptEngine = ScriptEngineManager().getEngineByName("kotlin")

        return Mono.just(engine.eval(event.args().joinToString(separator = " ").removeSurrounding("```")))
                .map { result -> event.replyEmbed(EmbedUtils.getEmbed(null, null, listOf(listOf("Your Code", "```kotlin\n${event.args().joinToString(separator = " ")}\n```", false), listOf("Your Result", "```fix\n$result\n```", false)))).subscribe() }.then()
    }


    override fun runCommand(event: MessageCreateEvent, vararg args: String) {
        if (event.hasArgs()) super.runCommand(event)
    }
}