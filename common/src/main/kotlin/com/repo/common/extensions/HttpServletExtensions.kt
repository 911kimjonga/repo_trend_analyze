package com.repo.common.extensions

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

fun HttpServletRequest.getCookie(
    cookieName: String
) =
    cookies?.firstOrNull { it.name == cookieName }?.value

fun HttpServletResponse.addCookie(
    key: String,
    value: String,
    times: Int
) {
    val cookie = Cookie(key, value)
    cookie.isHttpOnly = true
    cookie.secure = true
    cookie.path = "/"
    cookie.maxAge = times
    addCookie(cookie)
}