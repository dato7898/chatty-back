package com.dato.chatty.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        e: AuthenticationException
    ) {
        logger.error("Responding with unauthorized error. Message - {}", e.message)
        httpServletResponse.sendError(
            HttpServletResponse.SC_UNAUTHORIZED,
            e.localizedMessage
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint::class.java)
    }
}