package com.dato.chatty.repo

import com.dato.chatty.model.Post
import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepo : MongoRepository<Post, String> {

    fun findAllById(ids: HashSet<String?>): List<Post>

}