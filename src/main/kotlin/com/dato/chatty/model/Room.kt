package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class Room {
    @Id
    var id: String? = null
    var users = ArrayList<User>()
    var isMultiChat: Boolean = false
    var deleted: Boolean = false
    var createdAt = Date()
    var editedAt = Date()
}
