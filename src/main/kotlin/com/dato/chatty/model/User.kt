package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    var email: String,
    var firstname: String,
    var lastname: String,
    var googleImgUrl: String?,
    var roles: Set<Role>
) {

    @Id
    var id: String? = null
    @DBRef
    var friends: ArrayList<User> = ArrayList()
    var deleted: Boolean = false

}
