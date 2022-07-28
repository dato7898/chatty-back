package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.User
import com.dato.chatty.repo.UserRepo
import org.springframework.data.domain.Pageable
import org.springframework.messaging.simp.user.SimpUser
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.regex.Pattern
import java.util.stream.Collectors

@Service
class UserService(
    private val simpUserRegistry: SimpUserRegistry,
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

    fun findById(id: Long): Optional<User> {
        return userRepo.findById(id)
    }

    fun update(user: User) {
        userRepo.save(user)
    }

    @Transactional
    fun findFriends(email: String, page: Pageable): List<User> {
        val user = userRepo.findByEmail(email).orElseThrow{ ResourceNotFoundException("User", "email", email) }
        return usersOnline(user.friends.stream().filter { it.friends.contains(user) }.collect(Collectors.toList()))
    }

    @Transactional
    fun findFriendsById(userId: Long, page: Pageable): List<User> {
        val user = userRepo.findById(userId).orElseThrow{ ResourceNotFoundException("User", "id", userId) }
        return usersOnline(user.friends.stream().filter { it.friends.contains(user) }.collect(Collectors.toList()))
    }

    @Transactional
    fun getFriendRequests(page: Pageable): List<User> {
        val user = getCurrentUser()
        return usersOnline(userRepo.findAllByFriendsContaining(user, page)
            .stream().filter { !user.friends.contains(it) }.collect(Collectors.toList()))
    }

    fun findUsers(search: String, page: Pageable): List<User> {
        val searches = search.split(Pattern.compile("\\s+")).map {"%$it%" }.toSet()
        return usersOnline(userRepo.findUsers(searches, page))
    }

    @Transactional
    fun addFriend(userId: Long): User {
        val currentUser = getCurrentUser()
        val user = userRepo.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId) }
        if (user == currentUser) {
            throw RuntimeException("Невозможно добавить себя в друзья")
        }
        currentUser.friends = currentUser.friends.plus(user)
        return userRepo.save(currentUser)
    }

    @Transactional
    fun deleteFriend(userId: Long): User {
        val currentUser = getCurrentUser()
        val user = userRepo.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId) }
        currentUser.friends = currentUser.friends.minus(user)
        return userRepo.save(currentUser)
    }

    fun usersOnline(users: Collection<User>): List<User> {
        val emails = simpUserRegistry.users.stream()
            .map(SimpUser::getName)
            .collect(Collectors.toList())
        return users.stream().map {
            it.online = emails.contains(it.email)
            it
        }.collect(Collectors.toList())
    }

}