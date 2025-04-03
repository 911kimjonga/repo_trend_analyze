package com.repo.integration.factory

import com.repo.integration.enums.Protocols
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import javax.net.ssl.TrustManager


object WebClientFactory {

    fun getDefaultClient(): WebClient = this.getWebClient()

    fun getWebClient(
        useSSL: Boolean = true,
        connectionTimeout: Int = 5000,
        readTimeout: Int = 3000,
        protocol: Protocols = Protocols.TLS_1_2,
        trustManagers: Array<TrustManager>? = null,
    ): WebClient {

        val httpClient = HttpClient.create()
            .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
            .responseTimeout(Duration.ofMillis(readTimeout.toLong()))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(readTimeout.toLong(), TimeUnit.MILLISECONDS))
                conn.addHandlerLast(WriteTimeoutHandler(readTimeout.toLong(), TimeUnit.MILLISECONDS))
            }

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeaders { headers ->
                headers.accept = listOf(MediaType.APPLICATION_JSON, MediaType.valueOf("text/json"))
                headers.contentType = MediaType.APPLICATION_JSON
            }
            .defaultUriVariables(emptyMap<String, Any>())
            .build()
    }

}