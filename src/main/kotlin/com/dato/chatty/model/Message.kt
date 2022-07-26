package com.dato.chatty.model

import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@Entity
class Message(
    @ManyToOne
    var user: User,
    @ManyToOne
    var room: Room,
    var text: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: String? = null
    var deleted: Boolean = false
    @OneToMany
    var files = HashSet<MessageFile>()
    var createdAt = Date()
    var editedAt = Date()
    @OneToMany
    var reads = ArrayList<User>()
}