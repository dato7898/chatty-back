package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.dato.chatty.model.Room
import com.dato.chatty.service.RoomService
import org.springframework.data.domain.PageRequest
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

    @PreAuthorize("hasAuthority('USER')")
    fun getMyRooms(pageNum: Int, pageSize: Int): List<Room> {
        return roomService.getMyRooms(PageRequest.of(pageNum, pageSize))
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getRoomById(roomId: String): Room {
        return roomService.getRoomById(roomId)
    }

}