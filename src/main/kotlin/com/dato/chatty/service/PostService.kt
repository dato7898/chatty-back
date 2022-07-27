package com.dato.chatty.service

import com.dato.chatty.model.Post
import com.dato.chatty.repo.PostRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepo: PostRepo,
    private val userService: UserService,
    private val fileService: FileService
) {

    @Transactional
    fun fetchPosts(text: String, page: Pageable): List<Post> {
        return postRepo.findAllByTextLikeIgnoreCase(text, page)
    }

    @Transactional
    fun addPost(post: Post): Post {
        val curUser = userService.getCurrentUser()
        fileService.checkFilesAndSave(post.files)
        post.userId = curUser.id
        return postRepo.save(post)
    }

}