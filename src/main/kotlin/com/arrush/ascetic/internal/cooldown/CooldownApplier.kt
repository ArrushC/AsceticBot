package com.arrush.ascetic.internal.cooldown

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class CooldownApplier {

    private val cooldowns: MutableMap<String, Instant> = mutableMapOf()

    fun applyCooldown(key: String, span: Long, unit: ChronoUnit) {
        this.cooldowns.putIfAbsent(key, Instant.now().plus(span, unit))
    }

    fun isOnCooldown(key: String): Boolean =
            if (this.cooldowns.containsKey(key) && this.remainingCooldownFor(key, TimeUnit.SECONDS) >= 0) {
                true
            } else {
                this.cooldowns.computeIfPresent(key) { _, _ -> this.cooldowns.remove(key)}
                false
            }


    fun remainingCooldownFor(key: String, unit: TimeUnit) = TimeUnit.SECONDS.convert(Duration.between(Instant.now(), this.cooldowns[key]!!).seconds, unit)
}