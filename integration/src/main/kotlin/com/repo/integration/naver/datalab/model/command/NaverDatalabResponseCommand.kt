package com.repo.integration.naver.datalab.model.command

import com.repo.integration.naver.datalab.enums.TimeUnit
import com.repo.integration.naver.datalab.model.data.NaverDatalabRequestData
import com.repo.integration.naver.datalab.model.data.NaverDatalabResponseData
import com.repo.integration.naver.datalab.model.extensions.toCommand
import kotlinx.serialization.Serializable

@Serializable
data class NaverDatalabResponseCommand(
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