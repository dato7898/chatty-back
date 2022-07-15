package com.dato.chatty.repo

import com.dato.chatty.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepo : MongoRepository<User, String> {

    fun findByEmail(email: String): Optional<User>

}