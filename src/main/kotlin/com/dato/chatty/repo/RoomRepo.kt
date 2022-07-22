package com.dato.chatty.repo

import com.dato.chatty.model.Room
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface RoomRepo : MongoRepository<Room, String> {

    @Query("{\$and:[{userIds:?0},{userIds:?1},{isMultiChat:false}]}")
    fun findRoomWithUser(curUserId: String?, userId: String?): Optional<Room>

    fun findByIdAndUserIds(roomId: String, userId: String?): Optional<Room>

}