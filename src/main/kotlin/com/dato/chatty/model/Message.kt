package com.dato.chatty.model

import java.util.*
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
    @Column(name = "created_at", columnDefinition = "timestamp with time zone")
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt = Date()
    @Column(name = "edited_at", columnDefinition = "timestamp with time zone")
    @Temporal(TemporalType.TIMESTAMP)
    var editedAt = Date()
    @ManyToMany
    var reads: Set<User> = HashSet()
    @ManyToMany
    var deletes: Set<User> = HashSet()
}