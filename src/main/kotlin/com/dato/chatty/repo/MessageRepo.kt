package com.dato.chatty.repo

import com.dato.chatty.model.Message
import com.dato.chatty.model.Room
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface MessageRepo : MongoRepository<Message, String> {

    fun findAllByRoomAndDeletedIsFalseOrderByCreatedAtDesc(room: Room, page: Pageable): List<Message>

}