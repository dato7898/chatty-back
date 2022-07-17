package com.dato.chatty.controller

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.User
import com.dato.chatty.security.CurrentUser
import com.dato.chatty.security.UserPrincipal
import com.dato.chatty.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(
    private val userService: UserService
) {

    @GetMapping("me")
    @PreAuthorize("hasAuthority('USER')")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): User {
        return userService.findByEmail(userPrincipal.name)
            .orElseThrow{ ResourceNotFoundException("User", "name", userPrincipal.name) }
    }

    @GetMapping("me/friends")
    @PreAuthorize("hasAuthority('USER')")
    fun getCurrentFriends(@CurrentUser userPrincipal: UserPrincipal): List<User> {
        return userService.findFriends(userPrincipal.name, Pageable.unpaged())
    }

    @GetMapping("{userId}/friends")
    @PreAuthorize("hasAuthority('USER')")
    fun getUserFriends(@PathVariable userId: String): List<User> {
        return userService.findFriendsById(userId, Pageable.unpaged())
    }

}