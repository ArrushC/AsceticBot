package com.arrush.database.user

enum class UserDatabase {
    INSTANCE;

    private val users: Map<Long, UserData> = mutableMapOf()


}