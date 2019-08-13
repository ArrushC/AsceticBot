package com.arrush.ascetic.internal.cooldown

import java.util.concurrent.TimeUnit

enum class CooldownManager {

    INSTANCE;

    private val cooldowns: MutableMap<String, Long> = mutableMapOf()

    fun addCooldown(key: String, span: Long, unit: TimeUnit) {
        this.cooldowns.putIfAbsent(key, System.currentTimeMillis() + unit.toMillis(span) )
    }


    fun isOnCooldown(key: String): Boolean {
        return if (this.cooldowns.containsKey(key)) {
            if (((System.currentTimeMillis() - this.cooldowns[key]!!) / 1000) >= 0) {
                true
            } else {
                this.cooldowns.remove(key)
                false
            }
        } else {false}
    }

    fun getRemainingCooldown(key: String, unit: TimeUnit): Long = TimeUnit.MILLISECONDS.convert(this.cooldowns[key]!!, unit)
}