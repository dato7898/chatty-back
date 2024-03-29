package com.dato.chatty.service

import com.dato.chatty.exception.NotAllowedException
import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Message
import com.dato.chatty.repo.MessageRepo
import com.dato.chatty.repo.RoomRepo
import com.dato.chatty.websocket.controller.WebSocketController
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MessageService(
    private val messageRepo: MessageRepo,
    private val roomService: RoomService,
    private val userService: UserService,
    private val fileService: FileService,
    private val roomRepo: RoomRepo,
    private val webSocketController: WebSocketController
) {

    @Transactional
    fun setRead(roomId: Long) {
        val curUser = userService.getCurrentUser()
        val messages = messageRepo.findAllByRoomIdAndReadsNotContainsAndDeletesNotContains(roomId, curUser, curUser)
        messages.forEach {
            it.reads = it.reads.plus(curUser)
        }
        messageRepo.saveAll(messages)
    }

    @Transactional
    fun getMessagesWithUser(userId: Long, page: Pageable): List<Message> {
        val room = roomService.getRoomWithUser(userId)
        val curUser = userService.getCurrentUser()
        val messages = messageRepo.findAllByRoomAndDeletesNotContainsOrderByCreatedAtDesc(room, curUser, page)
        return messages.reversed()
    }

    @Transactional
    fun getMessagesByRoomId(roomId: Long, page: Pageable): List<Message> {
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        val curUser = userService.getCurrentUser()
        if (!room.users.contains(curUser)) {
            throw NotAllowedException("You are not in room")
        }
        val messages = messageRepo.findAllByRoomAndDeletesNotContainsOrderByCreatedAtDesc(room, curUser, page)
        messages.forEach {
            if (!it.reads.contains(curUser)) {
                it.reads = it.reads.plus(curUser)
            }
        }
        messageRepo.saveAll(messages)
        return messages.reversed()
    }

    @Transactional
    fun addMessageToUser(message: Message, userId: Long): Message {
        if (message.text.isBlank()) {
            throw RuntimeException("Message text cannot be empty")
        }
        val curUser = userService.getCurrentUser()
        fileService.checkFilesAndSave(message.files)
        val room = roomService.getRoomWithUser(userId)
        message.room = room
        message.user = curUser
        val newMessage = messageRepo.save(message)
        room.messages = room.messages.plus(newMessage)
        roomRepo.save(room)
        webSocketController.sendMessage(message)
        return newMessage
    }

    @Transactional
    fun addMessageToRoom(message: Message, roomId: Long): Message {
        if (message.text.isBlank()) {
            throw RuntimeException("Message text cannot be empty")
        }
        val curUser = userService.getCurrentUser()
        fileService.checkFilesAndSave(message.files)
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        message.room = room
        message.user = curUser
        message.reads = setOf(curUser)
        val newMessage = messageRepo.save(message)
        room.messages = room.messages.plus(newMessage)
        roomRepo.save(room)
        webSocketController.sendMessage(message)
        return newMessage
    }

    @Transactional
    fun deleteMessage(messageId: Long): Boolean {
        val curUser = userService.getCurrentUser()
        val message = messageRepo.findById(messageId).orElseThrow {
            ResourceNotFoundException("Message", "id", messageId)
        }
        if (message.user?.id != curUser.id) {
            throw throw NotAllowedException("That not your own message")
        }
        message.deletes = message.deletes.plus(curUser)
        messageRepo.save(message)
        return true
    }

}