package com.repo.auth.core.filter

import com.repo.common.response.ApiResponse
import com.repo.auth.core.token.provider.AccessTokenProvider
import com.repo.auth.domain.user.enums.UserRole
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthFilter(
    private val accessTokenProvider: AccessTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val requiredRole = this.resolveRequiredRole(request)

            when (requiredRole) {
                UserRole.GUEST -> {
                    filterChain.doFilter(request, response)
                    return
                }

                else -> {}
            }

            val token: String = accessTokenProvider.extractToken(request)

            accessTokenProvider.validateToken(token, requiredRole)

            val id = accessTokenProvider.getIdByToken(token)

            val auth = UsernamePasswordAuthenticationToken(
                id,
                null,
                listOf(
                    SimpleGrantedAuthority("ROLE_${requiredRole.role}")
                )
            )

            SecurityContextHolder.getContext().authentication = auth

            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()

            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"

            val body = ApiResponse.error(HttpStatus.UNAUTHORIZED.value().toString(), e.message.orEmpty())

            response.writer.write(Json.encodeToString(body))
            response.writer.flush()
        }
    }

    private fun resolveRequiredRole(request: HttpServletRequest): UserRole {
        val uri = request.requestURI
        val method = request.method

        return when {
            uri.startsWith("/admin") -> UserRole.ADMIN
            uri.startsWith("/user") -> UserRole.USER
            uri.startsWith("/auth/logout") -> UserRole.USER
            uri.startsWith("/auth/withdraw") -> UserRole.USER
            else -> UserRole.GUEST
        }
    }
}