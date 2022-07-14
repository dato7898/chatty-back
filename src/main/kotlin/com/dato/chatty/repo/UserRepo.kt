package com.dato.chatty.repo

import com.dato.chatty.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepo : MongoRepository<User, String> {
}