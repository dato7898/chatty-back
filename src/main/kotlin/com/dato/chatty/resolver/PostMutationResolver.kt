package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.dato.chatty.model.Post
import com.dato.chatty.service.PostService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class PostMutationResolver(
    private val postService: PostService
) : GraphQLMutationResolver {

    @PreAuthorize("hasAuthority('USER')")
    fun addPost(post: Post): Post {
        return postService.addPost(post)
    }

}