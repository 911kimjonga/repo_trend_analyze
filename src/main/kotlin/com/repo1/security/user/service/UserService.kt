package com.repo1.security.user.service

import com.repo1.security.user.model.User
import org.springframework.stereotype.Service

@Service
class UserService {
    
    fun getUser(): User = User("1", "테스터")
    
}