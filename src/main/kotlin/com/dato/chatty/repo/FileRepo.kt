package com.dato.chatty.repo

import com.dato.chatty.model.MessageFile
import org.springframework.data.mongodb.repository.MongoRepository

interface FileRepo : MongoRepository<MessageFile, String> {

    fun findAllByIdInAndStatus(ids: HashSet<String>, status: String): List<MessageFile>

}