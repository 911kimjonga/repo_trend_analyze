package com.repo.integration.naver.datalab.model

import com.repo.integration.naver.datalab.enums.TimeUnit
import kotlinx.serialization.Serializable

@Serializable
class NaverDatalabResponseData(
    val startDate: String,
    val endDate: String,
    val timeUnit: TimeUnit,
    val results: List<Result>
) {

    @Serializable
    data class Result(
        val title: String,
        val keywords: List<String>,
        val data: List<Data>,
    ) {

        @Serializable
        data class Data(
            val period: String,
            val ratio: Double,
        )

    }

}