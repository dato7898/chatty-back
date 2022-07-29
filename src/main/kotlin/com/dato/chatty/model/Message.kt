package com.dato.chatty.model

import java.io.Serializable
import java.time.OffsetDateTime
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
class Message(
    @ManyToOne
    var user: User? = null,
    @ManyToOne
    var room: Room? = null,
    var text: String = ""
) : Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var deleted: Boolean = false
    @OneToMany(fetch = FetchType.EAGER)
    var files: Set<MessageFile> = HashSet()
    var createdAt = OffsetDateTime.now()
    var editedAt = OffsetDateTime.now()
    @ManyToMany
    var reads: Set<User> = HashSet()
    @ManyToMany
    var deletes: Set<User> = HashSet()
}