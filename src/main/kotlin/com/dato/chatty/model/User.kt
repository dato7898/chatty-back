package com.dato.chatty.model

import java.util.Collections
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    var email: String,
    var firstname: String,
    var lastname: String,
    var googleImgUrl: String?,
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles")
    var roles: Set<Role> = Collections.singleton(Role.USER),
    var deleted: Boolean = false,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    @OneToMany
    @JoinTable(name="friends")
    @JoinColumn(name="user_A_id", referencedColumnName="id")
    @JoinColumn(name="user_B_id", referencedColumnName="id")
    var friends: ArrayList<User> = ArrayList()
}