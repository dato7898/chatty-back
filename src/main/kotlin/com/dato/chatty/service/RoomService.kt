package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Room
import com.dato.chatty.repo.MessageRepo
import com.dato.chatty.repo.RoomRepo
import org.bson.types.ObjectId
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
    @Synchronized fun getRoomWithUser(userId: String): Room {
        val curUser = userService.getCurrentUser()
        val user = userService.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId) }
        return roomRepo.findByUsersContainsAndIsMultiChatIsFalse(listOf(curUser, user))
            .orElseGet {
                val room = Room()
                room.users = arrayListOf(curUser, user)
                roomRepo.save(room)
            }
    }

    @Transactional
    fun getMyRooms(page: Pageable): List<Room> {
        val curUser = userService.getCurrentUser()
        val rooms = roomRepo.findAllByUsersContainsOrderByLastMessageAtDesc(curUser, page)
        rooms.forEach {
            it.unread = messageRepo.countAllUnread(ObjectId(it.id), listOf(ObjectId(curUser.id)))
        }
        return rooms
    }

    @Transactional
    fun getRoomById(roomId: String): Room {
        val curUser = userService.getCurrentUser()
        val room = roomRepo.findById(roomId).orElseThrow { ResourceNotFoundException("Room", "id", roomId) }
        if (!room.users.contains(curUser)) {
            throw RuntimeException("Not allowed")
        }
        return room
    }

    @Transactional
    fun checkUserInRoom(roomId: String, email: String): Boolean {
        val user = userService.findByEmail(email).orElseThrow { ResourceNotFoundException("User", "email", email) }
        val room = roomRepo.findByIdAndUsers(roomId, user)
        return room.isPresent
    }

}