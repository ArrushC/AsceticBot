package com.arrush.ascetic.internal.database.user


data class UserData(val id: Long, var exp: Long=0, var lvl: Long=0, var isPremium: Boolean=false) {
    var expData = ExpData(this)
}