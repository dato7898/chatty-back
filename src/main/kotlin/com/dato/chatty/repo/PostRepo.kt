package com.dato.chatty.repo

import com.dato.chatty.model.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepo : JpaRepository<Post, Long> {

    fun findAllByTextLikeIgnoreCase(text: String, page: Pageable): List<Post>

}