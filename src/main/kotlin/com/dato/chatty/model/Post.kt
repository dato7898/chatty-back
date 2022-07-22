package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document
data class Post(
    val text: String,
    var userId: String?
) {

    @Id
    var id: String? = null
    var deleted = false
    var createdAt = Date()
    var editedAt = Date()
    var fileIds = HashSet<String>()

}