package com.arrush.ascetic.listeners.eschedule

class ScheduleStatus(val status: Status, val code: Int=0) {


    fun isCompleted() = this.status == Status.COMPLETED

    enum class Status {
        COMPLETED,
        REPEAT,
        FAILED,
        SKIPPED,
        NOT_COMPLETED
    }
}