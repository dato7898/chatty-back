package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(var email: String, var firstname: String, var lastname: String, var roles: Set<Role>) {

    @Id
    var id: String? = null
    var friendIds: HashSet<String> = HashSet()

}
