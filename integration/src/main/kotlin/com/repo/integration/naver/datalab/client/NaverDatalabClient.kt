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
            url = config.url,
            path = config.datalab.path,
            method = HttpMethod.valueOf(config.datalab.method),
            contentType = MediaType.valueOf(config.datalab.contentType),
            accept = MediaType.valueOf(config.datalab.accept),
            customHeaders = clientHeader,
            body = data
        )

        val response = webClient.execute(
            request = requestData,
            responseType = NaverDatalabResponseData::class
        )

        return response.toCommand()
    }

}