package com.dato.chatty.websocket.listener

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.repo.UserRepo
import com.dato.chatty.security.UserPrincipal
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.simp.user.SimpUser
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent


@Component
class WebSocketEventListener(
    private var messagingTemplate: SimpMessageSendingOperations,
    private val simpUserRegistry: SimpUserRegistry,
    private var userRepo: UserRepo
) {

    private val objectMapper = ObjectMapper()

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        send("ONLINE", event)
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        send("OFFLINE", event)
    }

    fun send(type: String, event: AbstractSubProtocolEvent) {
        val stompAccessor = StompHeaderAccessor.wrap(event.message)
        val currentAuthentication  = stompAccessor.getHeader("simpUser") as UsernamePasswordAuthenticationToken
        val curEmail = (currentAuthentication.principal as UserPrincipal).name
        val curUser = userRepo.findByEmail(curEmail).orElseThrow { ResourceNotFoundException("User", "email", curEmail) }
        curUser.friends = emptySet()
        curUser.online = true
        val jsonUser = objectMapper.writeValueAsString(curUser)
        simpUserRegistry.users.stream()
            .map(SimpUser::getName)
            .forEach {
                messagingTemplate.convertAndSendToUser(it, "/msg", "{\"type\":\"$type\",\"payload\":$jsonUser}")
            }
    }

}