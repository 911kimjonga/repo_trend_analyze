package com.repo.messaging.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("kafka/practice")
class KafkaPracticeController {

    @GetMapping
    fun practice(): ResponseEntity<String> {

        return ResponseEntity.ok("ok")
    }

}