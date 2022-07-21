package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.FileStatus
import com.dato.chatty.model.Message
import com.dato.chatty.repo.FileRepo
import com.dato.chatty.repo.MessageRepo
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.data.domain.Pageable
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.user.SimpUser
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.HtmlUtils
import java.util.stream.Collectors

@Service
class MessageService(
    private val messageRepo: MessageRepo,
    private val roomService: RoomService,
    private val userService: UserService,
    private val simpUserRegistry: SimpUserRegistry,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val taskExecutor: SimpleAsyncTaskExecutor,
    private val fileRepo: FileRepo
) {

    @Transactional
    fun getMessagesWithUser(userId: String, page: Pageable): List<Message> {
        val room = roomService.getRoomWithUser(userId)
        return messageRepo.findAllByRoomId(room.id, page)
    }

    @Transactional
    fun addMessageToUser(message: Message, userId: String): Message {
        if (message.text.isBlank()) {
            throw RuntimeException("Message text cannot be empty")
        }
        val curUser = userService.getCurrentUser()
        message.fileIds.forEach {
            val file = fileRepo.findById(it).orElseThrow { ResourceNotFoundException("MessageFile", "id", it) }
            if (curUser.id != file.senderId) {
                throw RuntimeException("Not allowed")
            }
            file.status = FileStatus.SAVED.name
            fileRepo.save(file)
        }
        val room = roomService.getRoomWithUser(userId)
        message.roomId = room.id
        message.senderId = curUser.id
        val newMessage = messageRepo.save(message)
        taskExecutor.execute { sendWebsocketMessage(curUser.email, message.text, room.id) }
        return newMessage
    }

    @Transactional
    fun deleteMessage(messageId: String): Boolean {
        val curUser = userService.getCurrentUser()
        val message = messageRepo.findById(messageId).orElseThrow {
            ResourceNotFoundException("Message", "id", messageId)
        }
        if (message.senderId != curUser.id) {
            throw RuntimeException("Operation not allowed")
        }
        messageRepo.delete(message)
        return true
    }

    fun sendWebsocketMessage(email: String, message: String, roomId: String?) {
            val subscribers = simpUserRegistry.users.stream()
                .map(SimpUser::getName)
                .filter { email != it }
                .collect(Collectors.toList())

            subscribers.forEach {
                simpMessagingTemplate.convertAndSendToUser(it, "/msg/$roomId",
                    "Hello, " + HtmlUtils.htmlEscape(message) + "!")
            }
    }

}