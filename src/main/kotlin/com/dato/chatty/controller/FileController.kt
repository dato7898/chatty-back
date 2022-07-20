package com.dato.chatty.controller

import com.dato.chatty.service.FileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("file")
class FileController(
    private val fileService: FileService
) {

    @PostMapping("upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile) {
        fileService.uploadFile(file.originalFilename, file.contentType, file.bytes)
    }

    @GetMapping("download/{fileId}")
    fun downloadFile(
        @PathVariable fileId: String,
        response: HttpServletResponse
    ) {
        fileService.downloadFile(fileId, response)
    }
}