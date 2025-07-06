package com.repo.messaging.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentLinkedQueue

@Component
class KafkaPracticeConsumer {

    private val consumedMessages = ConcurrentLinkedQueue<String>()

    @KafkaListener(topics = ["practice-topic"], groupId = "practice-group")
    fun listen(message: String) {
        println("Consumed: $message")
        consumedMessages.add(message)
    }

    fun getMessages(): List<String> = consumedMessages.toList()
}