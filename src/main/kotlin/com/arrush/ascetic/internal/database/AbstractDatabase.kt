package com.arrush.ascetic.internal.database

abstract class AbstractDatabase<D, E> {
    abstract fun loadData()
    abstract fun modify(data: D)
    abstract fun save(entity: E): Boolean
    fun saveMultiple(entities: List<E>): Boolean {
        var isFalse = false
        for (data in entities) {
            val result = this.save(data)
            if (!isFalse) isFalse = result
        }
        return isFalse
    }
}