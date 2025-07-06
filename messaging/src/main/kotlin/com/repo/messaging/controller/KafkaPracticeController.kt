package com.repo.messaging.controller

import com.repo.messaging.consumer.KafkaPracticeConsumer
import com.repo.messaging.producer.KafkaPracticeProducer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("kafka/practice")
class KafkaPracticeController(
    private val producer: KafkaPracticeProducer,
    private val consumer: KafkaPracticeConsumer
) {

    @PostMapping
    fun sendMessage(@RequestParam message: String): ResponseEntity<String> {
        producer.send(message)
        return ResponseEntity.ok("Message sent: $message")
    }

    @GetMapping
    fun getMessages(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(consumer.getMessages())
    }

}