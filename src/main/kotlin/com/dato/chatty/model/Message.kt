package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Message(
    var senderId: String?,
    var roomId: String?,
    var text: String
) {

    @Id
    var id: String? = null
    var deleted: Boolean = false

}