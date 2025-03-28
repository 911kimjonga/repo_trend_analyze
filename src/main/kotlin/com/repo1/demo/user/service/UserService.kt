package com.repo1.demo.user.service

import com.repo1.demo.user.model.User
import org.springframework.stereotype.Service

@Service
class UserService {
    
    fun getUser(): User = User("1", "테스터")
    
}