package com.dato.chatty.repo

import com.dato.chatty.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepo : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    fun findAllByFriendsContaining(user: User, page: Pageable): List<User>

    @Query("FROM users WHERE lower(email) LIKE ANY (?0) OR lower(firstname) LIKE ANY (?0) OR lower(lastname) LIKE ANY (?0)")
    fun findUsers(search: Set<String>, page: Pageable): List<User>

}