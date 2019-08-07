package com.arrush.threadding

import com.arrush.ascetic.Constants
import com.arrush.logger.Logger

class RestartThread(name: String): Thread(name) {
    private val logger = Constants.LOGGER.get() as Logger

    override fun run() {
        logger.warn("I am going to revive in few moments after I die. Just remember that there can be complications in this process.")
        //Runtime.getRuntime().exec(Constants.START_CMD.getString())
    }
}