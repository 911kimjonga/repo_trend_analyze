package com.repo.integration.naver.datalab.controller

import com.repo.integration.naver.datalab.service.NaverDatalabService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/naver-datalab")
class NaverDatalabController(
    private val service: NaverDatalabService,
    private val json: Json
) {

    @GetMapping("/test")
    suspend fun test(): ResponseEntity<String> {

        val result = json.encodeToString(service.search())

        return ResponseEntity.ok(result)
    }

}