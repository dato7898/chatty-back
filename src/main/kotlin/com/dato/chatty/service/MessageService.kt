package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Message
import com.dato.chatty.repo.MessageRepo
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.data.domain.Pageable
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.user.SimpUser
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class MessageService(
    private val messageRepo: MessageRepo,
    private val roomService: RoomService,
    private val userService: UserService,
    private val simpUserRegistry: SimpUserRegistry,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val taskExecutor: SimpleAsyncTaskExecutor,
    private val fileService: FileService
) {

    @Transactional
    fun getMessagesWithUser(userId: String, page: Pageable): List<Message> {
        val room = roomService.getRoomWithUser(userId)
        return messageRepo.findAllByRoomIdAndDeletedIsFalse(room.id, page)
    }

    @Transactional
    fun addMessageToUser(message: Message, userId: String): Message {
        if (message.text.isBlank()) {
            throw RuntimeException("Message text cannot be empty")
        }
        val curUser = userService.getCurrentUser()
        fileService.checkFilesAndSave(message.fileIds)
        val room = roomService.getRoomWithUser(userId)
        message.roomId = room.id
        message.user = curUser
        val newMessage = messageRepo.save(message)
        taskExecutor.execute { sendWebsocketMessage(curUser.email, newMessage, room.id) }
        return newMessage
    }

    @Transactional
    fun deleteMessage(messageId: String): Boolean {
        val curUser = userService.getCurrentUser()
        val message = messageRepo.findById(messageId).orElseThrow {
            ResourceNotFoundException("Message", "id", messageId)
        }
        if (message.user?.id != curUser.id) {
            throw RuntimeException("Operation not allowed")
        }
        message.deleted = true
        messageRepo.save(message)
        return true
    }

    fun sendWebsocketMessage(email: String, message: Message, roomId: String?) {
            val subscribers = simpUserRegistry.users.stream()
                .map(SimpUser::getName)
                .filter { email != it }
                .collect(Collectors.toList())

            subscribers.forEach {
                simpMessagingTemplate.convertAndSendToUser(it, "/msg/$roomId", message)
            }
    }

}