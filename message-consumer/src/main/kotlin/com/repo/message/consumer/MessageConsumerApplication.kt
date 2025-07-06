package com.repo.message.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.repo.message.consumer", "com.repo.common"])
class MessageConsumerApplication

fun main(args: Array<String>) {
    runApplication<MessageConsumerApplication>(*args)
}