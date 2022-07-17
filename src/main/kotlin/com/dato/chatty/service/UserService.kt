package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.User
import com.dato.chatty.repo.UserRepo
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.regex.Pattern

@Service
class UserService(
    private val userRepo: UserRepo
) {

    fun getCurrentUser(): User {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getName)
            .flatMap(userRepo::findByEmail)
            .orElseThrow { ResourceNotFoundException("User", "email", "") }
    }

    fun findByEmail(email: String): Optional<User> {
        return userRepo.findByEmail(email)
    }

    fun update(user: User) {
        userRepo.save(user)
    }

    fun findFriends(email: String, page: Pageable): List<User> {
        val user = userRepo.findByEmail(email).orElseThrow{ RuntimeException() }
        return userRepo.findAllByIdInAndFriendIds(user.friendIds, user.id, page)
    }

    fun findFriendsById(userId: String, page: Pageable): List<User> {
        val user = userRepo.findById(userId).orElseThrow{ RuntimeException() }
        return userRepo.findAllByIdInAndFriendIds(user.friendIds, user.id, page)
    }

    fun getFriendRequests(page: Pageable): List<User> {
        val user = getCurrentUser()
        return userRepo.findAllByFriendIdsAndIdNotIn(user.id, user.friendIds, page)
    }

    fun findUsers(search: String, page: Pageable): List<User> {
        val searches = search.split(Pattern.compile("\\s+")).map {
            Pattern.compile(".*$it.*")
        }.toSet()
        return userRepo.findUsers(searches, page)
    }

    fun addFriend(userId: String): User {
        val currentUser = getCurrentUser()
        userRepo.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId) }
        currentUser.friendIds.add(userId)
        return userRepo.save(currentUser)
    }

    fun deleteFriend(userId: String): User {
        val currentUser = getCurrentUser()
        userRepo.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId) }
        currentUser.friendIds.remove(userId)
        return userRepo.save(currentUser)
    }

}