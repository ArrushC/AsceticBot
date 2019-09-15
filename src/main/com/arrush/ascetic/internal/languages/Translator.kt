package com.arrush.ascetic.internal.languages

object Translator {

    private val languages: Map<String, Language> = mapOf(
            "en" to LanguageEnglish()
    )

    // methods that return TranslationAction.
    fun any(language: String, key: String): TranslationAction = this.languages[language]?.let { TranslationAction(it, key) }!!
    fun english(key: String): TranslationAction = this.any("en", key)
    fun french(key: String): TranslationAction = this.any("fr", key)


}