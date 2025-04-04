package com.repo.integration.factory

import com.repo.integration.enums.Protocols
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import reactor.netty.http.client.HttpClient
import java.io.FileInputStream
import java.security.KeyStore
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory


object WebClientFactory {

    fun getDefaultClient(): WebClient = this.getWebClient()

    fun getWebClient(
        useSSL: Boolean = true,
        connectionTimeout: Int = 5000,
        readTimeout: Int = 3000,
        protocol: Protocols = Protocols.TLS_1_2,
        trustManagerFactory: TrustManagerFactory = InsecureTrustManagerFactory.INSTANCE
    ): WebClient {

        var httpClient = HttpClient.create()
            .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
            .responseTimeout(Duration.ofMillis(readTimeout.toLong()))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(readTimeout.toLong(), TimeUnit.MILLISECONDS))
                conn.addHandlerLast(WriteTimeoutHandler(readTimeout.toLong(), TimeUnit.MILLISECONDS))
            }

        if (useSSL) {
            val sslContext = SslContextBuilder.forClient()
                .trustManager(trustManagerFactory)
                .protocols(protocol.protocol)
                .build()

            httpClient = httpClient.secure { spec ->
                spec.sslContext(sslContext)
            }
        }

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeaders { headers ->
                headers.accept = listOf(MediaType.APPLICATION_JSON, MediaType.valueOf("text/json"))
                headers.contentType = MediaType.APPLICATION_JSON
            }
            .build()
    }

    fun createTrustManagerFactory(
        trustStorePath: String,
        trustStorePassword: String,
        trustStoreType: String = "JKS" // 또는 "PKCS12"
    ): TrustManagerFactory {
        val keyStore = KeyStore.getInstance(trustStoreType)
        keyStore.load(FileInputStream(trustStorePath), trustStorePassword.toCharArray())

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)

        return trustManagerFactory
    }

}