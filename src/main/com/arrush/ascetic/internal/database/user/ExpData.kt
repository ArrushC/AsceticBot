package com.arrush.ascetic.internal.database.user

import com.arrush.ascetic.AsceticBot
import kotlin.math.roundToLong

data class ExpData(private val userData: UserData) {
    fun getExp() = this.userData.exp
    fun getLvl() = this.userData.lvl

    fun addExp(exp: Long): ExpData {
        this.userData.exp += exp
        return this
    }

    fun addLevel(level: Long=1): ExpData {
        this.userData.lvl += level
        return this
    }

    fun levelUp(success: (ExpData) -> Unit) {
        val expToNext = this.expToNext()
        if ((this.userData.exp / expToNext) >= 1L) { // user has arrived to a new level.
            this.addLevel()
            success(this)
        }
    }

    fun expToNext(): Long {
        var percentage = 1.2
        if (this.userData.lvl >= 5) {
            percentage += ((this.userData.lvl / 5) / 5)
        }
        return (percentage * (this.userData.lvl + 1) * 100).roundToLong()
    }


    fun save() {
        AsceticBot.userDb.modify(userData)
    }
}