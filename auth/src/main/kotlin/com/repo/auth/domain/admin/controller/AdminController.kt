package com.repo.auth.domain.admin.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController {

    @PostMapping("/check")
    fun check(
        @AuthenticationPrincipal userId: String
    ): ResponseEntity<String> {

        return ResponseEntity.ok("user id: $userId")
    }

}