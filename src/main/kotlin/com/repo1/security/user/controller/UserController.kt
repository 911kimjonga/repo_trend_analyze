package com.repo1.security.user.controller

import com.repo1.security.user.model.dto.UserRequestDto
import com.repo1.security.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/sign")
    fun sign(
        @RequestParam(value = "name", required = false) name: String,
        @RequestParam(value = "email", required = false) email: String
    ): String {
        val save = userService.saveUser(UserRequestDto(name, email))

        return if (save) "success" else "failed"
    }

    @GetMapping("/login")
    fun login(
        @RequestParam(value = "name", required = false) name: String,
    ): String {

        return userService.findUserByUserName(name).toString()
    }

}