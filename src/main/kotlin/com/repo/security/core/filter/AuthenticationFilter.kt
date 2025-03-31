package com.repo.security.core.filter

import com.repo.security.core.jwt.enums.JwtHeaders
import com.repo.security.core.jwt.provider.JwtProvider
import com.repo.security.domain.user.enums.UserRole
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter(
    private val provider: JwtProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token: String = provider.extractToken(request.getHeader(JwtHeaders.AUTH.header))

            provider.validateToken(token, UserRole.USER)

            val id = provider.getIdByToken(token)

            val auth = UsernamePasswordAuthenticationToken(
                id,
                null,
                listOf(
                    SimpleGrantedAuthority("ROLE_${UserRole.USER.role}")
                )
            )

            SecurityContextHolder.getContext().authentication = auth

            filterChain.doFilter(request, response)
        } catch (e: RuntimeException) {
            SecurityContextHolder.clearContext()
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
        }
    }
}