package com.arrush.ascetic.utility

import com.arrush.ascetic.Constants
import discord4j.core.spec.EmbedCreateSpec
import java.awt.Color
import java.time.Instant

object EmbedUtils {
    fun getEmbed(title: String = "", description: String = "", author: List<String> = emptyList(), url: String = "", timestamp: Instant = Instant.now(), footer: List<String?> = emptyList(), imageUrl: String = "", thumbnailUrl: String = "", fields: List<List<Any>> = emptyList()): (EmbedCreateSpec) -> Unit = {
        it.setColor(Constants.COLOUR.get() as Color)
        it.setTimestamp(timestamp)

        if (title.isNotEmpty()) it.setTitle(title)
        if (description.isNotEmpty()) it.setDescription(description)
        if (author.isNotEmpty()) it.setAuthor(author[0], author[1], author[2])
        if (url.isNotEmpty()) it.setUrl(url)
        if (footer.isNotEmpty()) it.setFooter(footer[0]!!, footer[1])
        if (imageUrl.isNotEmpty()) it.setImage(imageUrl)
        if (thumbnailUrl.isNotEmpty()) it.setThumbnail(thumbnailUrl)
        if (fields.isNotEmpty()) {
            for (field in fields) {
                it.addField(field[0].toString(), field[1].toString(), field[2].toString().toBoolean())
            }
        }
    }

    fun getEmbed(title: String?, description: String?, fields: List<List<Any>>, footer: List<String?> = emptyList()): (EmbedCreateSpec) -> Unit = {
        it.setColor(Constants.COLOUR.get() as Color)
        it.setTimestamp(Instant.now())

        if (!title.isNullOrEmpty()) it.setTitle(title)
        if (!description.isNullOrEmpty()) it.setDescription(description)

        for (field in fields) {
            it.addField(field[0].toString(), field[1].toString(), field[2].toString().toBoolean())
        }

        if (footer.isNotEmpty()) it.setFooter(footer[0]!!, footer[1])
    }
}