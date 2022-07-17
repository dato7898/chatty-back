package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.dato.chatty.model.User
import com.dato.chatty.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserQueryResolver(
    private val userService: UserService
) : GraphQLQueryResolver {

    @PreAuthorize("hasAuthority('USER')")
    fun getMe(): User {
        return userService.getCurrentUser()
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getMyFriends(pageNum: Int, pageSize: Int): List<User> {
        return userService.findFriends(userService.getCurrentUser().email, PageRequest.of(pageNum, pageSize))
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getFriendsById(id: String, pageNum: Int, pageSize: Int): List<User> {
        return userService.findFriendsById(id, PageRequest.of(pageNum, pageSize))
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getFriendRequests(pageNum: Int, pageSize: Int): List<User> {
        return userService.getFriendRequests(PageRequest.of(pageNum, pageSize))
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getUsersBySearch(search: String, pageNum: Int, pageSize: Int): List<User> {
        return userService.findUsers(search, PageRequest.of(pageNum, pageSize))
    }

}