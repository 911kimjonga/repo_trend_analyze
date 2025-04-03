package com.repo.security.core.token.extensions

import com.repo.security.common.exception.SecurityException.*
import com.repo.security.core.token.enums.AccessTokenHeaders
import com.repo.security.core.token.enums.RefreshTokenCookies
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

fun HttpServletRequest.getAccessTokenHeader() =
    getHeader(AccessTokenHeaders.AUTH.header)
        ?: throw UnauthenticatedException()

fun HttpServletRequest.getRefreshToken() =
    cookies?.firstOrNull { it.name == RefreshTokenCookies.REFRESH_TOKEN.cookie }?.value
        ?: throw UnauthenticatedException()

fun HttpServletResponse.addCookieRefreshToken(refreshToken: String) {
    val cookie = Cookie(RefreshTokenCookies.REFRESH_TOKEN.cookie, refreshToken)
    cookie.isHttpOnly = true
    cookie.secure = true
    cookie.path = "/"
    cookie.maxAge = 7 * 24 * 60 * 60
    addCookie(cookie)
}