package com.dato.chatty.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.OffsetDateTime
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var name = ""
    var imageUrl = ""
    @ManyToMany(fetch = FetchType.EAGER)
    var users: Set<User> = HashSet()
    var isMultiChat: Boolean = false
    var deleted: Boolean = false
    var createdAt = OffsetDateTime.now()
    var editedAt = OffsetDateTime.now()
    @Transient
    @JsonInclude
    var lastMessage: Message? = null
    @Transient
    @JsonInclude
    var unread = 0L
    @OneToMany
    var messages: Set<Message> = setOf()
}