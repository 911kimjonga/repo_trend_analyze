package com.repo.auth.domain.user.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
) {

    @PostMapping("/check")
    fun check(
        @AuthenticationPrincipal userId: String
    ): ResponseEntity<String> {
        return ResponseEntity.ok("user id: $userId")
    }

}