package com.dato.chatty.websocket.controller

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Message
import com.dato.chatty.model.WsMsgWrap
import com.dato.chatty.repo.RoomRepo
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.simp.user.SimpUser
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.Optional

@RestController
class WebSocketController(
    private val simpUserRegistry: SimpUserRegistry,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val roomRepo: RoomRepo
) {

    @MessageMapping("/message/{roomId}")
    fun receiveAndSendMessage(@DestinationVariable roomId: Long, message: String, headers: StompHeaderAccessor) {
        val user = Optional.ofNullable(headers.user).map(Principal::getName)
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        val userEmails = room.users.map { it.email }.toList()
        if (user.isPresent) {
            simpUserRegistry.users.stream()
                .map(SimpUser::getName)
                .filter { user.get() != it && userEmails.contains(it) }
                .forEach {
                    simpMessagingTemplate.convertAndSendToUser(it, "/msg", message)
                }
        }
    }

    @Async
    fun sendMessage(message: Message) {
        val userEmails = message.room?.users?.map {
            it.friends = setOf()
            it.email
        }?.toList()
        message.user?.friends = setOf()
        message.room?.messages = setOf()
        simpUserRegistry.users.stream()
            .map(SimpUser::getName)
            .filter { message.user?.email != it && userEmails?.contains(it) == true }
            .forEach {
                simpMessagingTemplate.convertAndSendToUser(it, "/msg", WsMsgWrap("MESSAGE", message))
            }
    }

}