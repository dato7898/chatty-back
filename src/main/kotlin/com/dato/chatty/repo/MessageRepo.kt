package com.dato.chatty.repo

import com.dato.chatty.model.Message
import com.dato.chatty.model.Room
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MessageRepo : JpaRepository<Message, Long> {

    fun findAllByRoomAndDeletedIsFalseOrderByCreatedAtDesc(room: Room, page: Pageable): List<Message>

    //@Query(value = "{\$and: [{'room.\$id': ?0}, {'reads.\$id': {\$nin: ?1}}, {deleted: false}]}", count = true)
    @Query("FROM message WHERE message.room.id=?0 AND message.reads.id NOT IN ?1 AND deleted IS FALSE")
    fun countAllUnread(roomId: Long?, userIds: List<Long?>): Long

    //@Query("{\$and: [{'room.\$id': ?0}, {'reads.\$id': {\$nin: ?1}}, {deleted: false}]}")
    fun getAllUnread(roomId: Long?, userIds: List<Long?>): List<Message>

}