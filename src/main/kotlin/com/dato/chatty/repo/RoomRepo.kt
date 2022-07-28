package com.dato.chatty.repo

import com.dato.chatty.model.Room
import com.dato.chatty.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface RoomRepo : JpaRepository<Room, Long> {

    fun findByUsersContainsAndUsersContainsAndIsMultiChatIsFalse(user1: User, user2: User): Optional<Room>

    fun findByIdAndUsersContains(roomId: Long, user: User): Optional<Room>

    @Query("WITH max_date AS (SELECT MAX(created_at) createdAt, room_id FROM message m " +
            "WHERE :userId NOT IN (SELECT md.deletes_id FROM message_deletes md WHERE m.id=md.message_id) GROUP BY room_id) " +
            "SELECT * FROM room r JOIN max_date md ON r.id = md.room_id " +
            "WHERE :userId IN (SELECT ru.users_id FROM room_users ru WHERE r.id=ru.room_id) " +
            "ORDER BY md.createdAt DESC", nativeQuery = true)
    fun findRoomsByUserId(@Param("userId") userId: Long?, page: Pageable): List<Room>

}