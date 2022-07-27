package com.dato.chatty.repo

import com.dato.chatty.model.MessageFile
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepo : JpaRepository<MessageFile, Long> {

    fun findAllByIdInAndStatus(ids: List<Long?>, status: String): List<MessageFile>

}