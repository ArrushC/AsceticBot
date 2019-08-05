package com.arrush.ascetic.internal.command

enum class CommandCategory(val type: String, val description: String) {
    FUN("Fun", "Fun commands to help you enjoy."),
    DEVELOPERS("Developers", "Commands that can only be used by developers."),
    MODERATION("Moderation", "Moderating and administrating the guild in all possible angles."),
    UTILITY("Utility", "Utilising the bot for your needs.")
}