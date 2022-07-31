package com.dato.chatty.model

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class MessageFile : Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var googleFileId: String = ""
    var contentType: String? = null
    var fileName: String? = null
    var senderId: Long? = null
    var status: String = FileStatus.UPLOADED.name
    var fileType: String? = FileType.DOCUMENT.name

}