package com.repo.common.logs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)

inline fun <reified T> T.logInfo(message: String, vararg params: Any) {
    logger().info(message, params)
}

inline fun <reified T> T.logDebug(message: String, vararg params: Any) {
    logger().debug(message, params)
}

inline fun <reified T> T.logWarn(message: String, vararg params: Any) {
    logger().warn(message, params)
}

inline fun <reified T> T.logError(message: String, throwable: Throwable? = null) {
    when (throwable) {
        null -> logger().error(message)
        else -> logger().error(message, throwable)
    }
}