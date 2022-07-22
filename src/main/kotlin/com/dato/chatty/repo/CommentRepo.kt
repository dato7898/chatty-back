package com.dato.chatty.repo

import com.dato.chatty.model.Comment
import org.springframework.data.mongodb.repository.MongoRepository

interface CommentRepo : MongoRepository<Comment, String> {
}