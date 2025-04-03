package com.repo.integration.client

import com.repo.integration.factory.WebClientFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class CommonWebClient(
    private val webClient: WebClient = WebClientFactory.getDefaultClient(),
) {

    /*
    fun <T : Any> execute(
        request: ApiSoiRequest,
        responseType: KClass<T>
    ): Mono<T> {
        val url = setUrl(request)
        val method = request.method
        val headers = setHeaders(request)
        val requestBody = setBody(request)

        return webClient.method(method)
            .uri(url)
            .headers { it.setAll(headers.toSingleValueMap()) }
            .apply {
                if (requestBody != null) {
                    this.bodyValue(requestBody)
                }
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .map { json.decodeFromString(responseType.serializer(), it) }
            .onErrorMap { ApiSoiResponseException("Api Soi 통신 실패", it) }
    }

    private fun setUrl(request: ApiSoiRequest): String =
        UriComponentsBuilder.fromHttpUrl(request.url)
            .path(request.path)
            .toUriString()
            .let { baseUrl ->
                request.body?.takeIf { request.method == HttpMethod.GET }
                    ?.let { toUriWithQueryParam(baseUrl, it) } ?: baseUrl
            }

    private fun setHeaders(request: ApiSoiRequest) =
        HttpHeaders().apply {
            this.contentType = request.contentType
            this.accept = listOf(request.accept)
            add(HEADER_X_OCB_CRYPTED_SID, request.sid)
            add(HEADER_X_OCB_AGENT, request.agentHeader)
        }

    private fun setBody(request: ApiSoiRequest): Any? =
        when {
            request.method == HttpMethod.GET -> null
            request.contentType == MediaType.APPLICATION_FORM_URLENCODED -> toFormUrlEncoded(request.body)
            else -> request.body
        }

    private fun <T : Any> toUriWithQueryParam(
        baseUrl: String,
        request: T
    ): String {
        val uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)

        request::class.memberProperties.forEach { property ->
            val value = property.getter.call(request)
            if (value != null) {
                val key = property.findAnnotation<SerialName>()?.value ?: property.name
                uriBuilder.queryParam(key, value)
            }
        }

        return uriBuilder.toUriString()
    }

    private fun toFormUrlEncoded(requestBody: Any?): MultiValueMap<String, String>? {
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
     */
}