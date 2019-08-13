package com.arrush.ascetic.commands.`fun`

import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import reactor.core.publisher.Mono
import kotlin.random.Random

class EightBallCommand : Command("8ball", CommandCategory.FUN, "8ball <question>", "Ask a question and unfold the possibilites.") {

    private val choices: List<String> = listOf(
            "yes", "no", "definitely", "never", "undoubtedly", "no u",
            "Not really", "Not answering that question", "Not sure", "Maybe",
            "Its uncertain", "No, not in a million years"
    )

    override fun onCommand(event: CommandEvent): Mono<Void> = event.replyEmbed(event.getEmbed(
            description = choices[Random.nextInt(0, choices.size -1)],
            title = "Response to ${event.authorName}'s question",
            footer = listOf(event.args.joinToString(separator = " ").removeSurrounding(" "), null, null))
    ).then()

    override fun runCommand(event: CommandEvent) {
        if (event.hasArgs) super.runCommand(event)
    }
}