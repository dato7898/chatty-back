package com.dato.chatty.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
class Message(
    @ManyToOne
    var user: User? = null,
    @ManyToOne
    var room: Room? = null,
    var text: String = ""
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var deleted: Boolean = false
    @OneToMany(fetch = FetchType.EAGER)
    var files: Set<MessageFile> = HashSet()
    var createdAt = LocalDateTime.now()
    var editedAt = LocalDateTime.now()
    @ManyToMany
    var reads: Set<User> = HashSet()
    @ManyToMany
    var deletes: Set<User> = HashSet()
}