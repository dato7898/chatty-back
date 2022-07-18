package com.dato.chatty.repo

import com.dato.chatty.model.Message
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface MessageRepo : MongoRepository<Message, String> {

    fun findAllByRoomId(id: String?, page: Pageable): List<Message>

}