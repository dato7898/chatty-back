package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.dato.chatty.model.Message
import com.dato.chatty.service.MessageService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class MessageMutationResolver(
    private val messageService: MessageService
) : GraphQLMutationResolver {

    @PreAuthorize("hasAuthority('USER')")
    fun addMessageToUser(message: Message, userId: Long): Message {
        return messageService.addMessageToUser(message, userId)
    }

    @PreAuthorize("hasAuthority('USER')")
    fun addMessageToRoom(message: Message, roomId: Long): Message {
        return messageService.addMessageToRoom(message, roomId)
    }

    @PreAuthorize("hasAuthority('USER')")
    fun deleteMessage(messageId: Long): Boolean {
        return messageService.deleteMessage(messageId)
    }

    @PreAuthorize("hasAuthority('USER')")
    fun setRead(roomId: Long): Boolean {
        messageService.setRead(roomId)
        return true
    }

}