package com.repo.integration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.repo.integration", "com.repo.common"])
class IntegrationApplication

fun main(args: Array<String>) {
    runApplication<IntegrationApplication>(*args)
}