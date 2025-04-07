package com.repo.integration.naver.datalab.service

import com.repo.integration.naver.datalab.client.NaverDatalabClient
import com.repo.integration.naver.datalab.enums.TimeUnit
import com.repo.integration.naver.datalab.model.data.NaverDatalabRequestData
import com.repo.integration.naver.datalab.model.dto.NaverDatalabResponseDto
import com.repo.integration.naver.datalab.model.extensions.toDto
import org.springframework.stereotype.Service

@Service
class NaverDatalabService(
    private val client: NaverDatalabClient
) {

    suspend fun search1(): NaverDatalabResponseDto {

        val data1 = NaverDatalabRequestData(
            "2025-03-01",
            "2025-03-31",
            TimeUnit.WEEKLY,
            listOf(
                NaverDatalabRequestData.KeywordGroups(
                    "한글",
                    listOf("한글", "korean")
                )
            )
        )

        val result1 = client.searchKeyword(data1)

        return result1.toDto()
    }

    suspend fun search2(): NaverDatalabResponseDto {

        val data2 = NaverDatalabRequestData(
            "2025-04-01",
            "2025-04-07",
            TimeUnit.WEEKLY,
            listOf(
                NaverDatalabRequestData.KeywordGroups(
                    "한글",
                    listOf("한글", "korean")
                )
            )
        )
        val result2 = client.searchKeyword(data2)

        return result2.toDto()
    }

}