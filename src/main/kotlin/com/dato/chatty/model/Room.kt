package com.dato.chatty.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*
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
    var createdAt = Date()
    var editedAt = Date()
    @Transient
    @JsonInclude
    var lastMessage: Message? = null
    @Transient
    @JsonInclude
    var unread = 0L
    @OneToMany
    var messages: Set<Message> = setOf()
}