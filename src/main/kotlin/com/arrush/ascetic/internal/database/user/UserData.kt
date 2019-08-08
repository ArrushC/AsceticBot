package com.arrush.ascetic.internal.database.user

import me.arrush.javabase.annotations.SQLColumn

data class UserData(@SQLColumn(column = "id") val userId: Long,
               @SQLColumn(column = "exp") var exp: Long=0,
               @SQLColumn(column = "isPremium") var isPremium: Boolean=false)