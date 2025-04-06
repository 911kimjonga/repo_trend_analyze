package com.repo.auth.core.token.extensions

import com.repo.common.extensions.addCookie
import com.repo.common.extensions.getCookie
import com.repo.auth.common.exception.AuthException.RefreshTokenException.*
import com.repo.auth.common.exception.AuthException.AccessTokenException.*
import com.repo.auth.core.token.constants.ACCESS_TOKEN_HEADER_AUTH
import com.repo.auth.core.token.constants.REFRESH_TOKEN_COOKIE
import com.repo.auth.core.token.constants.REFRESH_TOKEN_TTL
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

fun HttpServletRequest.getAccessTokenHeader() =
    getHeader(ACCESS_TOKEN_HEADER_AUTH)
        ?: throw InvalidAccessTokenException()

fun HttpServletRequest.getRefreshToken() =
    getCookie(REFRESH_TOKEN_COOKIE)
        ?: throw InvalidRefreshTokenException()

fun HttpServletResponse.addCookieRefreshToken(refreshToken: String) =
    addCookie(REFRESH_TOKEN_COOKIE, refreshToken, REFRESH_TOKEN_TTL.toInt())
