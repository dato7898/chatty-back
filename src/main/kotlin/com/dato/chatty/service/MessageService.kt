package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Message
import com.dato.chatty.repo.MessageRepo
import com.dato.chatty.repo.RoomRepo
import org.bson.types.ObjectId
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

    // Тут возможна ошибка, что setRead отработает раньше чем сообщение добавится в базу
    // Фикс заключается в отправке сообщений в сокеты через бэк, когда сообщение уже сохранилось в базу, а не на клиенте
    @Transactional
    fun setRead(roomId: String) {
        val curUser = userService.getCurrentUser()
        val messages = messageRepo.getAllUnread(ObjectId(roomId), listOf(ObjectId(curUser.id)))
        messages.forEach {
            it.reads.add(curUser)
        }
        messageRepo.saveAll(messages)
    }

    @Transactional
    fun getMessagesWithUser(userId: String, page: Pageable): List<Message> {
        val room = roomService.getRoomWithUser(userId)
        val messages = messageRepo.findAllByRoomAndDeletedIsFalseOrderByCreatedAtDesc(room, page)
        return messages.reversed()
    }

    @Transactional
    fun getMessagesByRoomId(roomId: String, page: Pageable): List<Message> {
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        val curUser = userService.getCurrentUser()
        if (!room.users.contains(curUser)) {
            throw RuntimeException("Not allowed")
        }
        val messages = messageRepo.findAllByRoomAndDeletedIsFalseOrderByCreatedAtDesc(room, page)
        messages.forEach {
            if (!it.reads.contains(curUser)) {
                it.reads.add(curUser)
            }
        }
        messageRepo.saveAll(messages)
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
        message.room = room
        message.user = curUser
        message.reads = arrayListOf(curUser)
        val newMessage = messageRepo.save(message)
        room.lastMessage = newMessage
        room.lastMessageAt = Date()
        roomRepo.save(room)
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

}