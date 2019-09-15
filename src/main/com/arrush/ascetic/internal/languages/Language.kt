package com.arrush.ascetic.internal.languages

import de.arraying.kotys.JSON
import de.arraying.kotys.JSONArray
import java.io.File

abstract class Language(val type: String) {
    // The messages that are mapped from file.
    protected val messages: Map<String, Any> = JSON(File(this.url())).raw()

    // Simple methods that returns the absolute url of the location of the json files.
    fun url() = "languages/${this.type}.json"

    /**
     * Below are methods that determine how the message is retrieved.
     *
     * Since this is a immutable map of values from the json file, this
     * should be easier to extract and use them.
     */
    fun messagef(key: String, vararg args: Any): String = (this.messages.getOrElse(key) {"No translation for this"} as String).format(*args)
    fun messageArray(key: String): JSONArray = this.messages.getOrElse(key) {Throwable("Error occured whilst retrieving JSONArray")} as JSONArray
    fun messageArrayElement(key: String, index: Int): JSONArray = this.messageArray(key).array(index)
}