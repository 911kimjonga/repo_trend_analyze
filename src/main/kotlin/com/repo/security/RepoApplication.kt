package com.repo.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RepoApplication

fun main(args: Array<String>) {
    runApplication<RepoApplication>(*args)
}
