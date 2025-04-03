package com.repo.auth.core.token.extensions

import com.repo.common.extensions.addCookie
import com.repo.common.extensions.getCookie
import com.repo.auth.common.exception.AuthException.RefreshTokenException.*
import com.repo.auth.common.exception.AuthException.AccessTokenException.*
import com.repo.auth.core.token.enums.AccessTokenHeaders
import com.repo.auth.core.token.enums.RefreshTokenCookies
import com.repo.auth.core.token.provider.RefreshTokenProvider.Companion.REFRESH_TOKEN_EXPIRE_SECONDS
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

fun HttpServletRequest.getAccessTokenHeader() =
    getHeader(AccessTokenHeaders.AUTH.header)
        ?: throw InvalidAccessTokenException()

fun HttpServletRequest.getRefreshToken() =
    getCookie(RefreshTokenCookies.REFRESH_TOKEN.cookie)
        ?: throw InvalidRefreshTokenException()

fun HttpServletResponse.addCookieRefreshToken(refreshToken: String) =
    addCookie(RefreshTokenCookies.REFRESH_TOKEN.cookie, refreshToken, REFRESH_TOKEN_EXPIRE_SECONDS.toInt())
