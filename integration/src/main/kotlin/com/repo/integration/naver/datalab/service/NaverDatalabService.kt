package com.repo.integration.naver.datalab.service

import com.repo.integration.naver.datalab.client.NaverDatalabClient
import com.repo.integration.naver.datalab.enums.TimeUnit
import com.repo.integration.naver.datalab.model.data.NaverDatalabRequestData
import com.repo.integration.naver.datalab.model.dto.NaverDatalabResponseDto
import com.repo.integration.naver.datalab.model.extensions.toDto
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class NaverDatalabService(
    private val client: NaverDatalabClient,
    private val json: Json
) {

    suspend fun test(): String {
        return coroutineScope {
            val search1Deferred = async { search1() }
            val search2Deferred = async { search2() }

            val search1Result = search1Deferred.await()
            val search2Result = search2Deferred.await()

            val result1 = json.encodeToString(search1Result)
            val result2 = json.encodeToString(search2Result)

            result1 + "\n" + result2
        }
    }

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