package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Message(
    val senderId: String,
    val roomId: String,
    val text: String
) {

    @Id
    val id: String? = null

}