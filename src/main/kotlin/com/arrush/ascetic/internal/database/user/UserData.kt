package com.arrush.ascetic.internal.database.user

import me.arrush.javabase.annotations.SQLColumn

data class UserData(@SQLColumn(column = "id") val userId: Long,
                    @SQLColumn(column = "exp") internal var exp: Long=0,
                    @SQLColumn(column = "level") internal var lvl: Long=0,
                    @SQLColumn(column = "isPremium") var isPremium: Boolean=false) {

    //var expData = ExpData(this)
}