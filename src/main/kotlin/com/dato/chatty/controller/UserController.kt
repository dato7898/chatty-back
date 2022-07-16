package com.dato.chatty.controller

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.model.User
import com.dato.chatty.security.CurrentUser
import com.dato.chatty.security.UserPrincipal
import com.dato.chatty.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): User {
        return userService.findByEmail(userPrincipal.name)
            .orElseThrow{ ResourceNotFoundException("User", "name", userPrincipal.name) }
    }

}