package com.repo.integration.naver.datalab.model.extensions

import com.repo.integration.naver.datalab.model.command.NaverDatalabResponseCommand
import com.repo.integration.naver.datalab.model.data.NaverDatalabResponseData
import com.repo.integration.naver.datalab.model.dto.NaverDatalabResponseDto

fun NaverDatalabResponseData.toCommand(): NaverDatalabResponseCommand =
    NaverDatalabResponseCommand(
        startDate = this.startDate,
        endDate = this.endDate,
        timeUnit = this.timeUnit,
        results = this.results.map { result ->
            NaverDatalabResponseCommand.Result(
                title = result.title,
                keywords = result.keywords,
                data = result.data.map { data ->
                    NaverDatalabResponseCommand.Result.Data(
                        period = data.period,
                        ratio = data.ratio
                    )
                }
            )
        }
    )

fun NaverDatalabResponseCommand.toDto(): NaverDatalabResponseDto =
    NaverDatalabResponseDto(
        startDate = this.startDate,
        endDate = this.endDate,
        timeUnit = this.timeUnit,
        results = this.results.map { result ->
            NaverDatalabResponseDto.Result(
                title = result.title,
                keywords = result.keywords,
                data = result.data.map { data ->
                    NaverDatalabResponseDto.Result.Data(
                        period = data.period,
                        ratio = data.ratio
                    )
                }
            )
        }
    )