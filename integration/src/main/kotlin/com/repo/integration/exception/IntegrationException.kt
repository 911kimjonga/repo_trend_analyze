package com.repo.integration.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

sealed class IntegrationException(
    open val status: HttpStatus,
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    class IntegrationClientException(
        override val message: String = "Client error",
        override val cause: Throwable? = null,
    ) : IntegrationException(BAD_REQUEST, message, cause)

    class IntegrationServerException(
        override val message: String = "Server error",
        override val cause: Throwable? = null,
    ) : IntegrationException(INTERNAL_SERVER_ERROR, message, cause)

    class IntegrationTimeoutException(
        override val message: String = "Timeout",
        override val cause: Throwable? = null,
    ) : IntegrationException(GATEWAY_TIMEOUT, message, cause)

    class IntegrationConnectionException(
        override val message: String = "Connection error",
        override val cause: Throwable? = null,
    ) : IntegrationException(SERVICE_UNAVAILABLE, message, cause)

    class IntegrationUnexpectedException(
        override val message: String = "Unexpected error",
        override val cause: Throwable? = null,
    ) : IntegrationException(INTERNAL_SERVER_ERROR, message, cause)

    class IntegrationBusinessException(
        override val message: String = "Business error",
        override val cause: Throwable? = null,
    ) : IntegrationException(UNPROCESSABLE_ENTITY, message, cause)

}