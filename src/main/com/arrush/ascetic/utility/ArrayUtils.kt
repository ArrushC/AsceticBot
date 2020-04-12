@file:Suppress("unused")

package com.arrush.ascetic.utility

fun <E> MutableList<E>.getAndRemove(index: Int): E {
    val element = this[index]
    this.removeAt(index)
    return element
}

fun <E, K, V> List<E>.formMap(key: (E) -> K, value:  (E) -> V): MutableMap<K, V> {
    val map = mutableMapOf<K, V>()
    for (element in this) {
        map[key(element)] = value(element)
    }
    return map
}

fun <E> MutableList<E>.removeAllAndForm(predicate: (E) -> Boolean): MutableList<E> {
    this.removeAll(predicate)
    return this
}


fun Array<Any>.replaceMultiple(index: Int, toReplace: Map<String, String>): Array<Any> {
    for ((key, value) in toReplace) {
        this[index] = (this[index] as String).replace(key, value)
    }
    return this
}

fun Array<Any>.replace(index: Int, old: String, new: String): Array<Any> {
    this[index] = (this[index] as String).replace(old, new)
    return this
}

fun Array<Any>.replaceFieldValue(old: String, new: String): Array<Any> = this.replace(1, old, new)