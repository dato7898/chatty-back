package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.Room
import com.dato.chatty.repo.RoomRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoomService(
    private val roomRepo: RoomRepo,
    private val userService: UserService
) {

    @Transactional
    @Synchronized fun getRoomWithUser(userId: String): Room {
        val curUser = userService.getCurrentUser()
        val user = userService.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId) }
        return roomRepo.findRoomWithUser(curUser.id, user.id)
            .orElseGet {
                val room = Room()
                room.userIds = hashSetOf(curUser.id, user.id)
                roomRepo.save(room)
            }
    }

    @Transactional
    fun checkUserInRoom(roomId: String, email: String): Boolean {
        val user = userService.findByEmail(email).orElseThrow { ResourceNotFoundException("User", "email", email) }
        val room = roomRepo.findByIdAndUserIds(roomId, user.id)
        return room.isPresent
    }

}