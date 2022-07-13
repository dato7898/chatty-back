package application

import org.springframework.data.mongodb.repository.MongoRepository

interface MessageRepo : MongoRepository<Message, String> {
}