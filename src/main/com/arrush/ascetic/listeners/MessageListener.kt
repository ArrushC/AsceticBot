package com.arrush.ascetic.listeners

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.Constants
import com.arrush.ascetic.internal.CooldownApplier
import com.arrush.ascetic.internal.command.*
import com.arrush.ascetic.internal.languages.Translator
import com.arrush.ascetic.utility.DiscordUtils
import com.arrush.ascetic.utility.emptySnowflake
import com.arrush.ascetic.utility.getAndRemove
import discord4j.core.event.domain.message.MessageCreateEvent
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MessageListener: IListener {

    override fun onMessageCreate(event: MessageCreateEvent) {
        if (event.message.author.map { it.isBot }.orElse(false) ) return
        this.execCommand(event)
        this.addExp(event)
    }

    private fun execCommand(event: MessageCreateEvent) {
        val content = event.message.content.orElse("")!!
        val prefix = event.retrievePrefix()
        if (!content.startsWith(prefix)) return

        val args: MutableList<String> = event.args()
        val commandName = args.getAndRemove(0)

        if (!AsceticBot.INSTANCE.commandRegistry.commands.containsKey(commandName)) return
        if (AsceticBot.INSTANCE.isLockdown() && !DiscordUtils.isDeveloper(event.authorId())) return
        if (AsceticBot.INSTANCE.commandRegistry.commands[commandName]?.category == CommandCategory.DEVELOPERS && !DiscordUtils.isDeveloper(event.authorId())) return

        val key = "${event.member.map { it.id.asLong()}.orElse(0)}-cmd"
        if (Constants.getDevIds().values.contains(event.member.map { it.id}.orElse(emptySnowflake())) || !CooldownApplier.INSTANCE.isOnCooldown(key)) {
            AsceticBot.INSTANCE.commandRegistry.commands[commandName]!!.runCommand(event, *event.args().toTypedArray())
            if (!Constants.getDevIds().values.contains(event.member.map { it.id}.orElse(emptySnowflake()))) {
                CooldownApplier.INSTANCE.applyCooldown(key, 3, ChronoUnit.SECONDS)
            }
        } else {
            Translator.any(event.language(), "command.cooldown").send(event.channel(),
                    event.authorMention(),
                    CooldownApplier.INSTANCE.remainingCooldownFor(key, TimeUnit.SECONDS).toString()
            ).subscribe()
        }
    }

    private fun addExp(event: MessageCreateEvent) {
        val userId = Objects.requireNonNull(event.member.map { it.id }.get())
        val expData = Objects.requireNonNull(AsceticBot.INSTANCE.userDb.users[userId.asLong()]!!).expData // why does this not load lmao.
        val key = "${userId.asString()}-exp"

        if (!CooldownApplier.INSTANCE.isOnCooldown(key)) {
            expData.addExp(Random.nextLong(3, 6))
            CooldownApplier.INSTANCE.applyCooldown(key, 1, ChronoUnit.MINUTES)
        }

        expData.levelUp { e ->
            Translator.any(event.language(), "exp.levelup").send(event.channel(),
                    event.member.map { m -> m.mention }.orElse(""),
                    e.getLvl()).subscribe()
        }

        expData.save()
    }
}