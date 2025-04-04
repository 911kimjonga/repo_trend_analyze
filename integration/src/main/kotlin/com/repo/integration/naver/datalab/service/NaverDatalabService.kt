package com.repo.integration.naver.datalab.service

import com.repo.integration.naver.datalab.client.NaverDatalabClient
import com.repo.integration.naver.datalab.enums.TimeUnit
import com.repo.integration.naver.datalab.model.NaverDatalabRequestData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class NaverDatalabService(
    private val json: Json,
    private val client: NaverDatalabClient
) {

    fun search(): String? {
        val result = client.searchKeyword(
            NaverDatalabRequestData(
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
        )

        return json.encodeToString(result)
    }

}