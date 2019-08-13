package com.arrush.ascetic.internal.database.user

import com.arrush.ascetic.AsceticBot

data class ExpData(private val userData: UserData) {
    fun getExp() = this.userData.exp
    fun getLvl() = this.userData.lvl

    fun addExp(exp: Long): ExpData {
        this.userData.exp += exp
        this.save()
        return this
    }

    fun addLevel(level: Long=1): ExpData {
        this.userData.lvl += level
        this.save()
        return this
    }

    fun levelUp(success: (ExpData) -> Unit) {
        val expToNext = (this.userData.lvl + 1) * 100
        if ((this.userData.exp / expToNext) == 1L) { // user has arrived to a new level.
            this.addLevel()
            success(this)
        }
    }


    private fun save() {
        AsceticBot.INSTANCE.userDb.modify(userData)
    }
}