package com.dato.chatty.controller

import com.dato.chatty.model.Message
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.simp.user.SimpUser
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.HtmlUtils
import java.security.Principal
import java.util.Optional
import java.util.stream.Collectors

@RestController
class WebSocketController(
    private val simpUserRegistry: SimpUserRegistry,
    private val simpMessagingTemplate: SimpMessagingTemplate
) {

    @MessageMapping("/message/{roomId}")
    fun greeting(@DestinationVariable roomId: String, message: Message, headers: StompHeaderAccessor) {
        val user = Optional.ofNullable(headers.user).map(Principal::getName)
        if (user.isPresent) {
            val subscribers = simpUserRegistry.users.stream()
                .map(SimpUser::getName)
                .filter { user.get() != it }
                .collect(Collectors.toList())

            subscribers.forEach {
                simpMessagingTemplate.convertAndSendToUser(it, "/msg/$roomId", message)
            }
        }
    }

}