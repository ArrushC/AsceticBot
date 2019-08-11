package com.arrush.ascetic.internal.database

interface IDatabase<D, E> {
    fun loadData()
    fun modify(data: D)
    fun save(entity: E)
    fun saveMultiple(entities: List<E>) {
        for (data in entities) {
            this.save(data)
        }
    }
}