package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.collections.HashSet

@Document
class Message(
    var senderId: String?,
    var roomId: String?,
    var text: String
) {

    @Id
    var id: String? = null
    var deleted: Boolean = false
    var fileIds = HashSet<String>()
    var createdAt = Date()
    var editedAt = Date()

}