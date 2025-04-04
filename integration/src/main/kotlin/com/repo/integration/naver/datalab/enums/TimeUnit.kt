package com.repo.integration.naver.datalab.enums

import com.repo.integration.naver.datalab.serializer.TimeUnitSerializer
import kotlinx.serialization.Serializable

@Serializable(with = TimeUnitSerializer::class)
enum class TimeUnit(
    val unit: String
) {
    DAILY("date"),
    WEEKLY("week"),
    MONTHLY("month"),
    ;
}