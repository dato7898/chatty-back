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
    fun getRoomWithUser(userId: String): Room {
        val curUser = userService.getCurrentUser()
        val user = userService.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId) }
        return roomRepo.findRoomWithUsers(setOf(curUser.id, userId))
            .orElseGet {
                val room = Room()
                room.userIds = hashSetOf(curUser.id, userId)
                roomRepo.save(room)
            }
    }

}