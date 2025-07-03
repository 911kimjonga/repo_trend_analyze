package com.repo.integration.naver.datalab.model.dto

import com.repo.integration.naver.datalab.enums.TimeUnit
import com.repo.integration.naver.datalab.model.command.NaverDatalabResponseCommand
import com.repo.integration.naver.datalab.model.data.NaverDatalabResponseData
import com.repo.integration.naver.datalab.model.extensions.toCommand
import com.repo.integration.naver.datalab.model.extensions.toDto
import kotlinx.serialization.Serializable

@Serializable
data class NaverDatalabResponseDto(
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