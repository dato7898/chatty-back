package com.dato.chatty.model

import java.util.Date
import javax.persistence.*

@Entity
data class Post(
    var text: String = "",
    var userId: Long? = null
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var deleted = false
    @Column(name = "created_at", columnDefinition = "timestamp with time zone")
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt = Date()
    @Column(name = "edited_at", columnDefinition = "timestamp with time zone")
    @Temporal(TemporalType.TIMESTAMP)
    var editedAt = Date()
    @OneToMany
    var files: Set<MessageFile> = HashSet()

}