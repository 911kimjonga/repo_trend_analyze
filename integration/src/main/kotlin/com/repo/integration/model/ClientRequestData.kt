package com.repo.integration.model

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType

data class ClientRequestData(
    val url: String,
    val path: String,
    val method: HttpMethod,
    val contentType: MediaType,
    val accept: MediaType,
    val customHeaders: Map<String, String>,
    val body: Any? = null
)