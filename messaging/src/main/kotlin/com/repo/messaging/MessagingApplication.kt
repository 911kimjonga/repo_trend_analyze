package com.repo.messaging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.repo.messaging", "com.repo.common"])
class MessagingApplication

fun main(args: Array<String>) {
    runApplication<MessagingApplication>(*args)
}