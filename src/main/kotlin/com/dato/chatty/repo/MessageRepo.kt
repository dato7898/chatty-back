package com.dato.chatty.repo

import com.dato.chatty.model.Message
import com.dato.chatty.model.Room
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface MessageRepo : MongoRepository<Message, String> {

    fun findAllByRoomAndDeletedIsFalseOrderByCreatedAtDesc(room: Room, page: Pageable): List<Message>

    @Query(value = "{\$and: [{'room.\$id': ?0}, {'reads.\$id': {\$nin: ?1}}, {deleted: false}]}", count = true)
    fun countAllUnread(roomId: ObjectId, userIds: List<ObjectId>): Long

    @Query("{\$and: [{'room.\$id': ?0}, {'reads.\$id': {\$nin: ?1}}, {deleted: false}]}")
    fun getAllUnread(roomId: ObjectId, userIds: List<ObjectId>): List<Message>

}