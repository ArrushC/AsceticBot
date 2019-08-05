package com.arrush.ascetic.listeners.eschedule

class ScheduleStatus(var status: Status, val code: Int=0) {

    fun isCompleted() = this.status == Status.COMPLETED

    companion object {
        fun fromLogic(logic: Boolean) = if (logic) Status.COMPLETED.get() else Status.NOT_COMPLETED.get()
    }

    enum class Status {
        COMPLETED,
        REPEAT,
        FAILED,
        SKIPPED,
        NOT_COMPLETED;

        fun get(code: Int=0) = ScheduleStatus(this, code)
    }
}