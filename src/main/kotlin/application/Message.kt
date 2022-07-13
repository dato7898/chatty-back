package application

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Message(
    @Id
    val id: ObjectId = ObjectId.get(),
    val text: String)