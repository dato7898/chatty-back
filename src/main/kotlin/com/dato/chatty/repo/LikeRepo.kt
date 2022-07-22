package com.dato.chatty.repo

import com.dato.chatty.model.Like
import org.springframework.data.mongodb.repository.MongoRepository

interface LikeRepo : MongoRepository<Like, String> {
}