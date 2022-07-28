package com.dato.chatty.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.Collections
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    var email: String = "",
    var firstname: String = "",
    var lastname: String = "",
    var googleImgUrl: String? = null,
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles")
    var roles: Set<Role> = Collections.singleton(Role.USER),
    var deleted: Boolean = false,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    @ManyToMany
    var friends: Set<User> = HashSet()
    @Transient
    @JsonInclude
    var online = false
}