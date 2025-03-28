package com.repo1.security.user.controller

import com.repo1.security.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/login")
    fun login(): String {

        return userService.getUser().toString()
    }

}