package com.dato.chatty.model

import java.time.OffsetDateTime
import javax.persistence.*

@Entity
data class Post(
    var text: String = "",
    var userId: Long? = null
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var deleted = false
    var createdAt = OffsetDateTime.now()
    var editedAt = OffsetDateTime.now()
    @OneToMany
    var files: Set<MessageFile> = HashSet()

}