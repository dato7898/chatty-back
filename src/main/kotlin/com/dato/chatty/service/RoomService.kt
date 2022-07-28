package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Room
import com.dato.chatty.repo.MessageRepo
import com.dato.chatty.repo.RoomRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoomService(
    private val roomRepo: RoomRepo,
    private val userService: UserService,
    private val messageRepo: MessageRepo
) {

    @Transactional
    @Synchronized fun getRoomWithUser(userId: Long): Room {
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
    fun getMyRooms(page: Pageable): List<Room> {
        val curUser = userService.getCurrentUser()
        val rooms = roomRepo.findAllByUsersContainsOrderByLastMessageAtDesc(curUser, page)
        rooms.forEach {
            it.unread = messageRepo.countAllByRoomAndReadsNotContainsAndDeletedIsFalse(it, curUser)
            it.users = userService.usersOnline(it.users).toSet()
        }
        return rooms
    }

    @Transactional
    fun getRoomById(roomId: Long): Room {
        val curUser = userService.getCurrentUser()
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        if (!room.users.contains(curUser)) {
            throw RuntimeException("Not allowed")
        }
        room.unread = messageRepo.countAllByRoomAndReadsNotContainsAndDeletedIsFalse(room, curUser)
        return room
    }

}