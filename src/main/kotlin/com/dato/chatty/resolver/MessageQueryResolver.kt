package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.dato.chatty.model.Message
import com.dato.chatty.service.MessageService
import org.springframework.data.domain.PageRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class MessageQueryResolver(
    private val messageService: MessageService
) : GraphQLQueryResolver {

    @PreAuthorize("hasAuthority('USER')")
    fun getMessagesByUserId(userId: String, pageNum: Int, pageSize: Int): List<Message> {
        return messageService.getMessagesWithUser(userId, PageRequest.of(pageNum, pageSize))
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getMessagesByRoomId(roomId: String, pageNum: Int, pageSize: Int): List<Message> {
        return messageService.getMessagesByRoomId(roomId, PageRequest.of(pageNum, pageSize))
    }

}