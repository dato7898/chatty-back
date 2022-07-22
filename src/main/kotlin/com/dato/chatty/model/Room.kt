package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.collections.HashSet

@Document
class Room {
    @Id
    var id: String? = null
    var userIds: HashSet<String?> = HashSet()
    var isMultiChat: Boolean = false
    var deleted: Boolean = false
    var createdAt = Date()
    var editedAt = Date()
}
