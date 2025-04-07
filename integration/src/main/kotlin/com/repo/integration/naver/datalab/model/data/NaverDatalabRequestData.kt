package com.repo.integration.naver.datalab.model.data

import com.repo.integration.naver.datalab.enums.Device
import com.repo.integration.naver.datalab.enums.Gender
import com.repo.integration.naver.datalab.enums.TimeUnit
import com.repo.integration.naver.datalab.serializer.RequestDataAgesSerializer
import kotlinx.serialization.Serializable

@Serializable
data class NaverDatalabRequestData(
    val startDate: String,
    val endDate: String,
    val timeUnit: TimeUnit,
    val keywordGroups: List<KeywordGroups>,
    val device: Device? = null,
    val gender: Gender? = null,

    @Serializable(with = RequestDataAgesSerializer::class)
    val ages: List<String>? = null
) {

    @Serializable
    data class KeywordGroups(
        val groupName: String,
        val keywords: List<String>,
    )

}