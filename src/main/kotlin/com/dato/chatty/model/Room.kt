package com.dato.chatty.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import kotlin.collections.ArrayList

@Entity
class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var name = ""
    var imageUrl = ""
    @OneToMany
    var users = ArrayList<User>()
    var isMultiChat: Boolean = false
    var deleted: Boolean = false
    var createdAt = Date()
    var editedAt = Date()
    var lastMessageAt = Date()
    @OneToOne
    var lastMessage: Message? = null
    var unread = 0L
}