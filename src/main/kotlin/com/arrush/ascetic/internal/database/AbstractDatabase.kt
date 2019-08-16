package com.arrush.ascetic.internal.database

import me.arrush.javabase.entities.Column
import java.util.*

abstract class AbstractDatabase<D, E>(val tableName: String, val columns: LinkedHashMap<String, Column>) {

    abstract fun loadData()
    abstract fun modify(data: D)
    abstract fun save(entity: E)
    abstract fun saveMultiple(entities: List<E>)


    protected fun updateStatement(id: Long, fieldValues: LinkedHashSet<Pair<String, Any>>) = "UPDATE ${this.tableName} SET ${fieldValues.joinToString { it.first + "=" + (if (it.second is String) "\'${it.second}\'" else it.second) }} WHERE id = $id"
}