package com.dato.chatty.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class MessageFile(
    var googleFileId: String,
    var contentType: String,
    var fileName: String
) {

    @Id
    var id: String? = null
    var status: String = FileStatus.UPLOADED.name
    var fileType: String = FileType.DOCUMENT.name

}