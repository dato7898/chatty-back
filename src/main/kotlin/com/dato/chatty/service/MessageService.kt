package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Message
import com.dato.chatty.repo.MessageRepo
import com.dato.chatty.repo.RoomRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MessageService(
    private val messageRepo: MessageRepo,
    private val roomService: RoomService,
    private val userService: UserService,
    private val fileService: FileService,
    private val roomRepo: RoomRepo
) {

    @Transactional
    fun getMessagesWithUser(userId: String, page: Pageable): List<Message> {
        val room = roomService.getRoomWithUser(userId)
        val messages = messageRepo.findAllByRoomAndDeletedIsFalseOrderByCreatedAtDesc(room, page)
        return messages.reversed()
    }

    @Transactional
    fun getMessagesByRoomId(roomId: String, page: Pageable): List<Message> {
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        val messages = messageRepo.findAllByRoomAndDeletedIsFalseOrderByCreatedAtDesc(room, page)
        return messages.reversed()
    }

    @Transactional
    fun addMessageToUser(message: Message, userId: String): Message {
        if (message.text.isBlank()) {
            throw RuntimeException("Message text cannot be empty")
        }
        val curUser = userService.getCurrentUser()
        fileService.checkFilesAndSave(message.fileIds)
        val room = roomService.getRoomWithUser(userId)
        room.lastMessageAt = Date()
        message.room = room
        message.user = curUser
        return messageRepo.save(message)
    }

    @Transactional
    fun addMessageToRoom(message: Message, roomId: String): Message {
        if (message.text.isBlank()) {
            throw RuntimeException("Message text cannot be empty")
        }
        val curUser = userService.getCurrentUser()
        fileService.checkFilesAndSave(message.fileIds)
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        room.lastMessageAt = Date()
        message.room = room
        message.user = curUser
        return messageRepo.save(message)
    }

    @Transactional
    fun deleteMessage(messageId: String): Boolean {
        val curUser = userService.getCurrentUser()
        val message = messageRepo.findById(messageId).orElseThrow {
            ResourceNotFoundException("Message", "id", messageId)
        }
        if (message.user.id != curUser.id) {
            throw RuntimeException("Operation not allowed")
        }
        message.deleted = true
        messageRepo.save(message)
        return true
    }

}