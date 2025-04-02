package com.repo.security.core.token.extensions

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse

fun HttpServletResponse.addCookieRefreshToken(refreshToken: String) {
    val cookie = Cookie("refreshToken", refreshToken)
    cookie.isHttpOnly = true
    cookie.secure = true
    cookie.path = "/"
    cookie.maxAge = 7 * 24 * 60 * 60
    addCookie(cookie)
}