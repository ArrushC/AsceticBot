package com.arrush.ascetic.utility

import com.arrush.ascetic.Constants
import discord4j.core.`object`.util.Snowflake

class DiscordUtils {
    companion object {
        fun isDeveloper(authorId: Snowflake) = Constants.DEV_IDS.getMap().containsValue(authorId)
    }
}