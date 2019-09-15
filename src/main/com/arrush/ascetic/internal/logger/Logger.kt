package com.arrush.ascetic.internal.logger

import java.time.LocalDateTime

class Logger constructor(val name: String) {

    fun log(logType: LogType, message: String) {
        println("[${LocalDateTime.now()}] [$logType] [$name]: $message")
    }

    fun error(message: String) { this.log(LogType.ERROR, message) }
    fun debug(message: String) { this.log(LogType.DEBUG, message) }
    fun warn(message: String) { this.log(LogType.WARN, message) }
    fun info(message: String) { this.log(LogType.INFO, message) }
    fun trace(message: String) { this.log(LogType.TRACE, message) }
    fun success(message: String) {this.log(LogType.SUCCESS, message)}
}