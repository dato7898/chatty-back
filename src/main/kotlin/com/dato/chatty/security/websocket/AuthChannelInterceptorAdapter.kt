package com.dato.chatty.security.websocket

import com.dato.chatty.security.CustomOidcUserService
import com.dato.chatty.security.UserPrincipal
import com.dato.chatty.security.token.TokenProvider
import com.dato.chatty.service.RoomService
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class AuthChannelInterceptorAdapter(
    private val tokenProvider: TokenProvider,
    private val customOidcUserService: CustomOidcUserService,
    private val roomService: RoomService
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor: StompHeaderAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
        if (StompCommand.CONNECT == accessor.command) {
            val bearerToken = accessor.getFirstNativeHeader("Authorization")
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                val jwt = bearerToken.substring(7, bearerToken.length)
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                    val userName = tokenProvider.getUserNameFromToken(jwt)
                    val userDetails = customOidcUserService.loadUserByName(userName)
                    val user = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    accessor.user = user
                }
            }
        }
        if (StompCommand.SUBSCRIBE == accessor.command) {
            val currentAuthentication  = accessor.getHeader("simpUser") as UsernamePasswordAuthenticationToken
            val curEmail = (currentAuthentication.principal as UserPrincipal).name
            val destinationUrl = accessor.getHeader("simpDestination").toString()
            val pattern = "^/user/(.*?)/msg/(.*)$".toRegex()
            val match = pattern.find(destinationUrl)
            if (match != null) {
                val (email, roomId) = match.destructured
                if (email != curEmail) {
                    throw RuntimeException("Not allowed")
                }
                if (!roomService.checkUserInRoom(roomId, curEmail)) {
                    throw RuntimeException("Not allowed")
                }
            }
        }
        if (StompCommand.SEND == accessor.command) {
            val currentAuthentication  = accessor.getHeader("simpUser") as UsernamePasswordAuthenticationToken
            val curEmail = (currentAuthentication.principal as UserPrincipal).name
            val destinationUrl = accessor.getHeader("simpDestination").toString()
            if (destinationUrl.startsWith("/app/message/")) {
                val roomId = destinationUrl.substring(13)
                if (!roomService.checkUserInRoom(roomId, curEmail)) {
                    throw RuntimeException("Not allowed")
                }
            }
        }
        return message
    }

}