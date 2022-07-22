package com.dato.chatty.service

import com.dato.chatty.model.Post
import com.dato.chatty.model.elastic.ElasticPost
import com.dato.chatty.repo.PostRepo
import com.dato.chatty.repo.elastic.ElasticPostRepo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepo: PostRepo,
    private val elasticPostRepo: ElasticPostRepo,
    private val userService: UserService,
    private val fileService: FileService
) {

    @Transactional
    fun fetchPosts(text: String, pageable: Pageable): List<Post> {
        val ids: HashSet<String?> = elasticPostRepo.findByText(text, pageable).map { it.id }.toHashSet()
        return postRepo.findAllByIdIn(ids)
    }

    @Transactional
    fun addPost(post: Post): Post {
        val curUser = userService.getCurrentUser()
        fileService.checkFilesAndSave(post.fileIds)
        post.userId = curUser.id
        val newPost = postRepo.save(post)
        elasticPostRepo.save(ElasticPost(newPost.id, newPost.text))
        return newPost
    }

}