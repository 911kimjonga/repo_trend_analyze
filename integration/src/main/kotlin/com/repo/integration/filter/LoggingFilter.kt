package com.repo.integration.filter

import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import reactor.core.publisher.Mono
import java.net.InetAddress
import java.util.*
import java.util.concurrent.TimeUnit

object LoggingFilter {

    private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    private val hostName: String by lazy {
        try {
            InetAddress.getLocalHost().hostName
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private val isReal: Boolean by lazy {
        hostName.startsWith("OCBPRc-")
                && !hostName.contains("stg")
                && !hostName.contains("alp")
                && !hostName.contains("dev")
                && !hostName.contains("local")
    }

    fun logRequestAndResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction { request, next ->
            if (isReal) {
                return@ExchangeFilterFunction next.exchange(request)
            }

            val uuid = UUID.randomUUID().toString()
            val startTime = System.nanoTime()

            logRequest(uuid, request)

            next.exchange(request).flatMap { response ->
                logResponse(uuid, startTime, response)
            }
        }
    }

    private fun logRequest(uuid: String, request: ClientRequest) {
        val requestLog = buildString {
            append("$uuid\n--> ${request.method()} ${request.url()}")
            request.headers().forEach { (key, values) ->
                append("\n$key: ${values.joinToString("; ")}")
            }
            append("\n--> END ${request.method()}")
        }
        logger.info(requestLog)
    }

    private fun logResponse(uuid: String, startTime: Long, response: ClientResponse): Mono<ClientResponse> {
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)

        return response.bodyToMono(String::class.java)
            .defaultIfEmpty("")
            .flatMap { body ->
                val responseLog = buildString {
                    append("$uuid\n<-- ${response.statusCode()} (${tookMs}ms)")
                    response.headers().asHttpHeaders().forEach { (key, values) ->
                        append("\n$key: ${values.joinToString("; ")}")
                    }
                    append("\n\n$body")
                    append("\n<-- END HTTP")
                }
                logger.info(responseLog)

                Mono.just(
                    ClientResponse.create(response.statusCode())
                        .headers { headers -> headers.addAll(response.headers().asHttpHeaders()) }
                        .body(body)
                        .build()
                )
            }
    }

}