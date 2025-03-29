package com.repo.security.common.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)

inline fun <reified T> T.logInfo(message: String) {
    logger().info(message)
}

inline fun <reified T> T.logWarn(message: String) {
    logger().warn(message)
}

inline fun <reified T> T.logError(message: String, throwable: Throwable? = null) {
    when (throwable) {
        null -> logger().error(message)
        else -> logger().error(message, throwable)
    }
}