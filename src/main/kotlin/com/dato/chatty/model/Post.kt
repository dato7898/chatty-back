package com.dato.chatty.model

import java.util.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Post(
    var text: String = "",
    var userId: Long? = null
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var deleted = false
    var createdAt = Date()
    var editedAt = Date()
    @OneToMany
    var files: Set<MessageFile> = HashSet()

}