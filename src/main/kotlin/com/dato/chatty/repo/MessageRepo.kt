package com.dato.chatty.repo

import com.dato.chatty.model.Message
import com.dato.chatty.model.Room
import com.dato.chatty.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface MessageRepo : JpaRepository<Message, Long> {

    fun findAllByRoomAndDeletesNotContainsOrderByCreatedAtDesc(room: Room, user: User, page: Pageable): List<Message>

    fun countAllByRoomAndReadsNotContainsAndDeletesNotContains(room: Room, read: User, delete: User): Long

    fun findAllByRoomIdAndReadsNotContainsAndDeletesNotContains(roomId: Long, read: User, delete: User): List<Message>

    fun findAllByRoomAndDeletesNotContains(room: Room, user: User): List<Message>

    fun findFirstByRoomAndDeletesNotContainsOrderByCreatedAtDesc(room: Room, user: User): Optional<Message>

}