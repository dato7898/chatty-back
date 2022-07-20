package com.dato.chatty.controller

import com.dato.chatty.google.drive.GoogleDriveService
import org.apache.commons.io.IOUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("file")
class FileController(
    private val googleDriveService: GoogleDriveService
) {

    @PostMapping("upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile) {
        googleDriveService.uploadFile(file.originalFilename, file.contentType, file.bytes)
    }

    @GetMapping("download/{realFileId}")
    fun downloadFile(
        @PathVariable realFileId: String,
        response: HttpServletResponse
    ) {
        val baos = googleDriveService.downloadFile(realFileId)
        val content = baos.toByteArray()
        val bais = ByteArrayInputStream(content)
        IOUtils.copy(bais, response.outputStream)
        response.flushBuffer()
    }
}