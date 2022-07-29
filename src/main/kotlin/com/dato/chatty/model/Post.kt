package com.dato.chatty.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Post(
    var text: String = "",
    var userId: Long? = null
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var deleted = false
    var createdAt = LocalDateTime.now()
    var editedAt = LocalDateTime.now()
    @OneToMany
    var files: Set<MessageFile> = HashSet()

}