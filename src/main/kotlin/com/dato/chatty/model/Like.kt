package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Like(
    private val like: Int,
    private val userId: String?,
    private val entityId: String?
) {

    @Id
    var id: String? = null
    var likeType = LikeType.POST.name
    var createdAt = Date()
    var editedAt = Date()

}