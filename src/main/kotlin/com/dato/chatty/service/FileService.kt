package com.dato.chatty.service

import com.dato.chatty.exception.NotAllowedException
import com.dato.chatty.exception.ResourceNotFoundException
import com.dato.chatty.google.drive.GoogleDriveService
import com.dato.chatty.model.FileStatus
import com.dato.chatty.model.MessageFile
import com.dato.chatty.repo.FileRepo
import com.dato.chatty.repo.MessageRepo
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletResponse

@Service
class FileService(
    private val googleDriveService: GoogleDriveService,
    private val fileRepo: FileRepo,
    private val userService: UserService,
    private val messageRepo: MessageRepo
) {

    fun filesByMessageId(messageId: Long): List<MessageFile> {
        val curUser = userService.getCurrentUser()
        val message = messageRepo.findById(messageId).orElseThrow {
            ResourceNotFoundException("Message", "id", messageId)
        }
        if (curUser.id != message.user?.id) {
            throw NotAllowedException("That not your own message")
        }
        val fileIds = message.files.map { it.id }.toList()
        return fileRepo.findAllByIdInAndStatus(fileIds, FileStatus.SAVED.name)
    }

    @Transactional
    fun uploadFile(
        fileName: String?, contentType: String?,
        fileContent: ByteArray, fileType: String?
    ): MessageFile {
        val curUser = userService.getCurrentUser()
        val googleFileId = googleDriveService.uploadFile(fileName, contentType, fileContent)
        val newFile = MessageFile(googleFileId, contentType, fileName, curUser.id)
        if (StringUtils.hasText(fileType)) {
            newFile.fileType = fileType
        }
        return fileRepo.save(newFile)
    }

    @Transactional
    fun downloadFile(fileId: Long, response: HttpServletResponse) {
        val messageFile = fileRepo.findById(fileId).orElseThrow {
            ResourceNotFoundException("MessageFile", "id", fileId)
        }
        // TODO исправить, сделать проверку на комнату
        /*val curUser = userService.getCurrentUser()
        if (curUser.id != messageFile.senderId) {
            throw NotAllowedException("That not your own file")
        }*/
        response.contentType = messageFile.contentType
        response.setHeader("Content-Disposition", "attachment;filename=" + messageFile.fileName)
        val baos = googleDriveService.downloadFile(messageFile.googleFileId)
        val content = baos.toByteArray()
        val bais = ByteArrayInputStream(content)
        IOUtils.copy(bais, response.outputStream)
        response.flushBuffer()
    }

    fun checkFilesAndSave(files: Set<MessageFile>) {
        val curUser = userService.getCurrentUser()
        files.forEach {
            val file = fileRepo.findById(it.id).orElseThrow { ResourceNotFoundException("MessageFile", "id", it) }
            if (curUser.id != file.senderId) {
                throw NotAllowedException("That not your own file")
            }
            file.status = FileStatus.SAVED.name
            fileRepo.save(file)
        }
    }

}