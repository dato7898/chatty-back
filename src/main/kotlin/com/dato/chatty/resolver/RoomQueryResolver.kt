package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.dato.chatty.model.Room
import com.dato.chatty.service.RoomService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class RoomQueryResolver(
    private val roomService: RoomService
) : GraphQLQueryResolver {

    @PreAuthorize("hasAuthority('USER')")
    fun getRoomByUserId(userId: String): Room {
        return roomService.getRoomWithUser(userId)
    }

}