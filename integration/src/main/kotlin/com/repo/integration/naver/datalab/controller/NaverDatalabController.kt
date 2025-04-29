package com.repo.integration.naver.datalab.controller

import com.repo.integration.naver.datalab.service.NaverDatalabService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/naver-datalab")
class NaverDatalabController(
    private val service: NaverDatalabService,
) {

    @GetMapping("/test")
    suspend fun test(): ResponseEntity<String> {

        return ResponseEntity.ok(service.test())
    }

}