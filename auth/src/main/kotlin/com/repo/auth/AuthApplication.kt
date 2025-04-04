package com.repo.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.repo.auth", "com.repo.common"])
class AuthApplication

fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}