package com.repo.access.h2.database

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccessH2DatabaseApplication

fun main(args: Array<String>) {
    runApplication<AccessH2DatabaseApplication>(*args)
}