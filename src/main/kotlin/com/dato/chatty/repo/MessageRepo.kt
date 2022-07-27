package com.dato.chatty.repo

import com.dato.chatty.model.Message
import com.dato.chatty.model.Room
import com.dato.chatty.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepo : JpaRepository<Message, Long> {

    fun findAllByRoomAndDeletedIsFalseOrderByCreatedAtDesc(room: Room, page: Pageable): List<Message>

    fun countAllByRoomAndReadsNotContainsAndDeletedIsFalse(room: Room, user: User): Long

    fun findAllByRoomIdAndReadsNotContainsAndDeletedIsFalse(roomId: Long, user: User): List<Message>

}