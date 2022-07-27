package com.dato.chatty.repo

import com.dato.chatty.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepo : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    fun findAllByFriendsContaining(user: User, page: Pageable): List<User>

    @Query("FROM User WHERE lower(email) LIKE (?1) OR lower(firstname) LIKE (?1) OR lower(lastname) LIKE (?1)")
    fun findUsers(search: Set<String>, page: Pageable): List<User>

}