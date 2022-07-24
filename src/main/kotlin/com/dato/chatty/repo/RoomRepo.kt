package com.dato.chatty.repo

import com.dato.chatty.model.Room
import com.dato.chatty.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RoomRepo : MongoRepository<Room, String> {

    fun findByUsersContainsAndIsMultiChatIsFalse(users: List<User>): Optional<Room>

    fun findByIdAndUsers(roomId: String, user: User): Optional<Room>

    fun findAllByUsersContainsOrderByLastMessageAtDesc(user: User): List<Room>

}