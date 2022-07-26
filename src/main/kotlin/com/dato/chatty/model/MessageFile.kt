package com.dato.chatty.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class MessageFile(
    var googleFileId: String,
    var contentType: String?,
    var fileName: String?,
    var senderId: Long?
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var status: String = FileStatus.UPLOADED.name
    var fileType: String? = FileType.DOCUMENT.name

}