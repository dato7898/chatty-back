package com.dato.chatty.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.dato.chatty.model.MessageFile
import com.dato.chatty.service.FileService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class FileQueryResolver(
    private val fileService: FileService
) : GraphQLQueryResolver {

    @PreAuthorize("hasAuthority('USER')")
    fun getMessageFiles(messageId: String): List<MessageFile> {
        return fileService.filesByMessageId(messageId)
    }

}