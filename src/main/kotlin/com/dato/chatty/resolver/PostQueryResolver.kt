package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.dato.chatty.model.Post
import com.dato.chatty.service.PostService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class PostQueryResolver(
    private val postService: PostService
) : GraphQLQueryResolver {

    fun fetchPosts(text: String, pageNum: Int, pageSize: Int): List<Post> {
        return postService.fetchPosts(text, PageRequest.of(pageNum, pageSize))
    }

}