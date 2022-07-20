package com.dato.chatty.service

import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.google.drive.GoogleDriveService
import com.dato.chatty.repo.FileRepo
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletResponse

@Service
class FileService(
    private val googleDriveService: GoogleDriveService,
    private val fileRepo: FileRepo
) {

    fun uploadFile(fileName: String?, contentType: String?, fileContent: ByteArray) {

    }

    fun downloadFile(fileId: String, response: HttpServletResponse) {
        val messageFile = fileRepo.findById(fileId).orElseThrow {
            ResourceNotFoundException("MessageFile", "id", fileId)
        }
        response.contentType = messageFile.contentType;
        response.setHeader("Content-Disposition", "attachment;filename=" + messageFile.fileName);
        val baos = googleDriveService.downloadFile(messageFile.googleFileId)
        val content = baos.toByteArray()
        val bais = ByteArrayInputStream(content)
        IOUtils.copy(bais, response.outputStream)
        response.flushBuffer()
    }

}