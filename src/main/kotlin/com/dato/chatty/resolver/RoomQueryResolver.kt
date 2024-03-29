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
    fun getRoomByUserId(userId: Long): Room {
        return roomService.getRoomWithUser(userId)
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getMyRooms(pageNum: Int, pageSize: Int): List<Room> {
        return roomService.getMyRooms(pageNum, pageSize)
    }

    @PreAuthorize("hasAuthority('USER')")
    fun getRoomById(roomId: Long): Room {
        return roomService.getRoomById(roomId)
    }

}