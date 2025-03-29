package com.repo.security.user.controller

import com.repo.security.user.model.dto.SignInDto
import com.repo.security.user.model.dto.SignUpDto
import com.repo.security.user.model.vo.SignInVo
import com.repo.security.user.model.vo.SignUpVo
import com.repo.security.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/signup")
    fun sign(
        @RequestBody vo: SignUpVo
    ): ResponseEntity<String> {
        val save = userService.saveUser(
            SignUpDto(
                vo.username,
                vo.password,
                vo.email
            )
        )

        return ResponseEntity.ok(
            if (save) "success" else "failed"
        )
    }

    @PostMapping("/signin")
    fun login(
        @RequestBody vo: SignInVo
    ): ResponseEntity<String> {
        val isUser = userService.isUser(
            SignInDto(
                vo.username,
                vo.password
            )
        )

        return ResponseEntity.ok(
            if (isUser) "success" else "failed"
        )
    }

}