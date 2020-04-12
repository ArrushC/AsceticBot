package com.arrush.ascetic.utility

import com.arrush.ascetic.Constants
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.util.Snowflake
import reactor.core.publisher.Mono

class DiscordUtils {
    companion object {
        fun isDeveloper(authorId: Snowflake) = Constants.DEV_IDS.getMap().containsValue(authorId)
    }

}

fun Message.editContent(content: String): Mono<Message> = this.edit { it.setContent(content) }
