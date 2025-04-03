package com.repo.integration.enums

enum class Protocols(
    val protocol: String
) {
    TLS_1_1("TLSv1.1"),
    TLS_1_2("TLSv1.2"),
    TLS_1_3("TLSv1.3"),
    ;
}