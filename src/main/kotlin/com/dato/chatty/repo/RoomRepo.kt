package com.dato.chatty.repo

import com.dato.chatty.model.Room
import com.dato.chatty.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RoomRepo : JpaRepository<Room, Long> {

    fun findByUsersContainsAndUsersContainsAndIsMultiChatIsFalse(user1: User, user2: User): Optional<Room>

    fun findByUsersInAndIsMultiChatIsFalse(users: List<User>): Optional<Room>

    fun findByIdAndUsers(roomId: Long, user: User): Optional<Room>

    fun findAllByUsersContainsOrderByLastMessageAtDesc(user: User, page: Pageable): List<Room>

}