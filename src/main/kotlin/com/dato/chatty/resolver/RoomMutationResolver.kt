package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.dato.chatty.service.RoomService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class RoomMutationResolver(
    private val roomService: RoomService,
) : GraphQLMutationResolver {

    @PreAuthorize("hasAuthority('USER')")
    fun deleteRoom(roomId: Long): Boolean {
        roomService.deleteRoom(roomId)
        return true
    }

}