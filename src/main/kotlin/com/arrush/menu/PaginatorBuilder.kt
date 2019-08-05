package com.arrush.menu

import discord4j.core.spec.EmbedCreateSpec
import java.util.*

class PaginatorBuilder(val tag: String) {
    private val pages: MutableList<(EmbedCreateSpec) -> Unit>

    init {
        this.pages = ArrayList()
    }

    fun addItem(spec: (EmbedCreateSpec) -> Unit): PaginatorBuilder {
        this.pages.add(spec)
        return this
    }

    fun removeItem(index: Int): PaginatorBuilder {
        this.pages.removeAt(index)
        return this
    }

    fun build(): Paginator {
        return Paginator(tag, pages)
    }
}
