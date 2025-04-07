package com.repo.integration.naver.datalab.client

import com.repo.integration.client.IntegrationCoroutineWebClient
import com.repo.integration.model.IntegrationRequestData
import com.repo.integration.naver.datalab.config.NaverDatalabConfig
import com.repo.integration.naver.datalab.constants.X_NAVER_CLIENT_ID
import com.repo.integration.naver.datalab.constants.X_NAVER_CLIENT_SECRET
import com.repo.integration.naver.datalab.model.command.NaverDatalabResponseCommand
import com.repo.integration.naver.datalab.model.data.NaverDatalabRequestData
import com.repo.integration.naver.datalab.model.data.NaverDatalabResponseData
import com.repo.integration.naver.datalab.model.extensions.toCommand
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class NaverDatalabClient(
    private val config: NaverDatalabConfig,
    private val webClient: IntegrationCoroutineWebClient,
) {

    suspend fun searchKeyword(
        data: NaverDatalabRequestData
    ): NaverDatalabResponseCommand {
        val clientHeader: Map<String, String> = mapOf(
            X_NAVER_CLIENT_ID to config.client.id,
            X_NAVER_CLIENT_SECRET to config.client.secret,
        )

        val requestData = IntegrationRequestData(
            config.url,
            config.datalab.path,
            HttpMethod.valueOf(config.datalab.method),
            MediaType.valueOf(config.datalab.contentType),
            MediaType.valueOf(config.datalab.accept),
            clientHeader,
            data
        )

        val response = webClient.execute(requestData, NaverDatalabResponseData::class)

        return response.toCommand()
    }

}