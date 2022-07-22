package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.collections.HashSet

@Document
data class Comment(
    private val text: String,
    private val userId: String?,
    private val postId: String?
) {

    @Id
    var id: String? = null
    var deleted = false
    var createdAt = Date()
    var editedAt = Date()
    var fileIds = HashSet<String>()

}