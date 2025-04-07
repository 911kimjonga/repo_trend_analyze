package com.repo.integration.naver.datalab.controller

import com.repo.integration.naver.datalab.service.NaverDatalabService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@RestController
@RequestMapping("/naver-datalab")
class NaverDatalabController(
    private val service: NaverDatalabService,
    private val json: Json
) {

    @GetMapping("/test")
    suspend fun test(): ResponseEntity<String> {
        return coroutineScope {
            val search1Deferred = async { service.search1() }
            val search2Deferred = async { service.search2() }

            val search1Result = search1Deferred.await()
            val search2Result = search2Deferred.await()

            val result1 = json.encodeToString(search1Result)
            val result2 = json.encodeToString(search2Result)

            ResponseEntity.ok(result1 + "\n" + result2)
        }
    }

}