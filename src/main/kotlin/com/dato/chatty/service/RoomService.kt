package com.dato.chatty.service

import com.dato.chatty.exception.NotAllowedException
import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Room
import com.dato.chatty.repo.MessageRepo
import com.dato.chatty.repo.RoomRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoomService(
    private val roomRepo: RoomRepo,
    private val userService: UserService,
    private val messageRepo: MessageRepo
) {

    @Transactional
    fun getRoomWithUser(userId: Long): Room {
        val curUser = userService.getCurrentUser()
        val user = userService.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId) }
        return roomRepo.findByUsersContainsAndUsersContainsAndIsMultiChatIsFalse(curUser, user)
            .orElseGet {
                val room = Room()
                room.users = setOf(curUser, user)
                roomRepo.save(room)
            }
    }

    @Transactional
    fun getMyRooms(pageNum: Int, pageSize: Int): List<Room> {
        val curUser = userService.getCurrentUser()
        var rooms = roomRepo.findRoomsByUserId(curUser.id)
        rooms.forEach {
            it.unread = messageRepo.countAllByRoomAndReadsNotContainsAndDeletesNotContains(it, curUser, curUser)
            it.users = userService.usersOnline(it.users).toSet()
            it.lastMessage = messageRepo
                .findFirstByRoomAndDeletesNotContainsOrderByCreatedAtDesc(it, curUser)
                .orElse(null)
        }
        rooms = rooms.toMutableList()
        rooms.removeAll { it.lastMessage == null }
        return rooms
    }

    @Transactional
    fun getRoomById(roomId: Long): Room {
        val curUser = userService.getCurrentUser()
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        if (!room.users.contains(curUser)) {
            throw NotAllowedException("You are not in room")
        }
        room.unread = messageRepo.countAllByRoomAndReadsNotContainsAndDeletesNotContains(room, curUser, curUser)
        return room
    }

    @Transactional
    fun deleteRoom(roomId: Long) {
        val curUser = userService.getCurrentUser()
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        if (!room.users.contains(curUser)) {
            throw NotAllowedException("You are not in room")
        }
        val messages = messageRepo.findAllByRoomAndDeletesNotContains(room, curUser)
        messages.forEach {
            it.deletes = it.deletes.plus(curUser)
        }
        messageRepo.saveAll(messages)
    }

}