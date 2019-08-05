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

