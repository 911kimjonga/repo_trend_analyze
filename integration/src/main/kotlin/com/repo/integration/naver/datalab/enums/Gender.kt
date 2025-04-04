package com.repo.integration.naver.datalab.enums

import com.repo.integration.naver.datalab.serializer.GenderSerializer
import kotlinx.serialization.Serializable

@Serializable(with = GenderSerializer::class)
enum class Gender(
    val gender: String,
) {
    MALE("m"),
    FEMALE("f"),
    ;
}