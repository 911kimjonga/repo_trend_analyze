package com.repo.integration.client

import com.repo.common.logs.logError
import com.repo.common.logs.logInfo
import com.repo.integration.exception.IntegrationException.*
import com.repo.integration.factory.WebClientFactory
import com.repo.integration.model.IntegrationRequestData
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

@Component
class IntegrationCoroutineWebClient(
    private val json: Json,
) {
    private val webClient: WebClient = WebClientFactory.getDefaultClient()

    @OptIn(InternalSerializationApi::class)
    suspend fun <T : Any> execute(
        request: IntegrationRequestData,
        responseType: KClass<T>
    ): T {
        val url = this.setUrl(request)
        val method = request.method
        val headers = this.setHeaders(request)
        val requestBody = this.setBody(request)

        try {
            val response = webClient.method(method)
                .uri(url)
                .headers { it.setAll(headers.toSingleValueMap()) }
                .apply { requestBody?.let { this.bodyValue(it) } }
                .retrieve()
                .awaitBody<String>()

            return json.decodeFromString(responseType.serializer(), response)
        } catch (ex: Exception) {
            throw when (ex) {
                is WebClientResponseException -> when {
                    ex.statusCode.is4xxClientError -> IntegrationClientException()
                    ex.statusCode.is5xxServerError -> IntegrationServerException()
                    else -> IntegrationUnexpectedException()
                }

                is SocketTimeoutException -> IntegrationTimeoutException("Timeout", ex)
                is ConnectException -> IntegrationConnectionException("Connection error", ex)
                else -> IntegrationUnexpectedException("Unexpected error", ex)
            }
        }
    }

    private fun setUrl(
        request: IntegrationRequestData
    ): String {
        return UriComponentsBuilder.fromHttpUrl(request.url)
            .path(request.path)
            .toUriString()
            .let { baseUrl ->
                request.body?.takeIf { request.method == HttpMethod.GET }
                    ?.let { toUriWithQueryParam(baseUrl, it) } ?: baseUrl
            }
    }

    private fun setHeaders(
        request: IntegrationRequestData
    ): HttpHeaders {
        return HttpHeaders().apply {
            this.contentType = request.contentType
            this.accept = listOf(request.accept)

            val multiValueHeaders = LinkedMultiValueMap<String, String>()
            request.customHeaders.forEach { (key, value) ->
                multiValueHeaders.add(key, value)
            }

            addAll(multiValueHeaders)
        }
    }

    private fun setBody(
        request: IntegrationRequestData
    ): Any? {
        return when {
            request.method == HttpMethod.GET -> null
            request.contentType == MediaType.APPLICATION_FORM_URLENCODED -> toFormUrlEncoded(request.body)
            else -> request.body
        }
    }

    private fun <T : Any> toUriWithQueryParam(
        baseUrl: String,
        request: T
    ): String {
        val uriBuilder = UriComponentsBuilder.fromUriString(baseUrl)

        request::class.memberProperties.forEach { property ->
            val value = property.getter.call(request)
            if (value != null) {
                val key = property.findAnnotation<SerialName>()?.value ?: property.name
                uriBuilder.queryParam(key, value)
            }
        }

        return uriBuilder.toUriString()
    }

    private fun toFormUrlEncoded(
        requestBody: Any?
    ): MultiValueMap<String, String>? {
        if (requestBody == null) return null

        val map = LinkedMultiValueMap<String, String>()

        requestBody::class.memberProperties.forEach { property ->
            val value = property.getter.call(requestBody)?.toString()
            if (value != null) {
                val key = property.findAnnotation<SerialName>()?.value ?: property.name
                map.add(key, value)
            }
        }

        return map
    }
}