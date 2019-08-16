package com.arrush.ascetic.commands.`fun`

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.CommandEvent
import com.arrush.ascetic.utility.times
import reactor.core.publisher.Mono
import java.lang.Math.round

class ExpCommand : Command("exp", CommandCategory.FUN, "exp", "Check where how much exp you have...") {
    private val bars = 10

    override fun onCommand(event: CommandEvent): Mono<Void> {
        val data = AsceticBot.INSTANCE.userDb.users[event.authorId.asLong()]!!

        println("EXP: Okay")

        val totalExp = data.exp
        val level = data.lvl

        println("EXP: very beter")

        val expToNext = (level + 1) * 100
        val barsNeeded = round(((totalExp / expToNext).toDouble())) * this.bars
        val barsRemaining = this.bars - barsNeeded

        println("EXP: Close")
        return event.replyEmbed(event.getEmbed(
                title = "Your EXP Information",
                description = "Here I will tell you everything about your exp.",
                fields = listOf(listOf("Current EXP", "EXP: **${expToNext - totalExp}**\nTotal EXP: **$totalExp EXP**\nLevel: **$level**", false),
                        listOf("EXP To Next Level", "Remaining: **${expToNext - totalExp} EXP**\n```fix\n[${("=" * barsNeeded) + (" " * barsRemaining)}]\n```")
                ))).then()
    }
}

