package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.dato.chatty.model.User
import com.dato.chatty.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserMutationResolver(
    private val userService: UserService
) : GraphQLMutationResolver {

    @PreAuthorize("hasAuthority('USER')")
    fun addFriend(userId: Long): User {
        return userService.addFriend(userId)
    }

    @PreAuthorize("hasAuthority('USER')")
    fun deleteFriend(userId: Long): User {
        return userService.deleteFriend(userId)
    }

}