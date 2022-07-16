package com.dato.chatty.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenProvider {

    @Value("\${app.auth.tokenSecret}")
    private val tokenSecret: String = ""

    @Value("\${app.auth.tokenExpirationMsec}")
    private val tokenExpirationMsec: Long = 0

    fun createToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as DefaultOidcUser
        val now = Date()
        val expiryDate = Date(now.time + tokenExpirationMsec)
        return Jwts.builder()
            .setSubject(userPrincipal.email)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, tokenSecret)
            .compact()
    }

    fun getUserNameFromToken(token: String?): String {
        val claims: Claims = Jwts.parser()
            .setSigningKey(tokenSecret)
            .parseClaimsJws(token)
            .body
        return claims.subject
    }

    fun validateToken(authToken: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TokenProvider::class.java)
    }
}