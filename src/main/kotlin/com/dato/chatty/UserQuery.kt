package com.dato.chatty

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.dato.chatty.model.User
import com.dato.chatty.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserQuery(
    private val userService: UserService
) : GraphQLQueryResolver {

    @PreAuthorize("hasAuthority('USER')")
    fun getMe(): User {
        return userService.getCurrentUser()
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getMyFriends(): List<User> {
        return userService.findFriends(userService.getCurrentUser().email)
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getFriendsById(id: String): List<User> {
        return userService.findFriendsById(id)
    }

}