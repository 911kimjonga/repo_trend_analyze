package com.repo.security.user.controller

import com.repo.security.user.model.dto.SignDto
import com.repo.security.user.model.vo.SignVo
import com.repo.security.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/sign")
    fun sign(
        @RequestBody vo: SignVo
    ): String {
        val save = userService.saveUser(SignDto(vo.username, vo.password, vo.email))

        return if (save) "success" else "failed"
    }

    @GetMapping("/login")
    fun login(
        @RequestParam(value = "name", required = false) name: String,
    ): String {

        return userService.findUserByUsername(name).toString()
    }

}