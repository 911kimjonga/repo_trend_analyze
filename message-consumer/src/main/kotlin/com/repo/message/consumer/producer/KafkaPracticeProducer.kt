package com.repo.message.consumer.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaPracticeProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun send(message: String) {
        kafkaTemplate.send("practice-topic", message)
    }
}