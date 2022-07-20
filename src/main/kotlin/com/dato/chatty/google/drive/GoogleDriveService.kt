package com.dato.chatty.google.drive

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream

@Service
class GoogleDriveService {

    @Value("\${app.google.drive.parent}")
    private val googleDriveParent = ""

    private val service: Drive

    init {
        val credentials = GoogleCredentials.getApplicationDefault().createScoped(listOf(DriveScopes.DRIVE_FILE))
        val requestInitializer = HttpCredentialsAdapter(credentials)
        service = Drive.Builder(NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            requestInitializer)
            .setApplicationName("Drive Chatty")
            .build()
    }

    fun uploadFile(fileName: String?, contentType: String?, fileContent: ByteArray): String {
        val fileMetadata = File()
        fileMetadata.name = fileName
        fileMetadata.parents = listOf(googleDriveParent)
        val mediaContent = ByteArrayContent(contentType, fileContent)
        try {
            val file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute()
            return file.id
        } catch (e: GoogleJsonResponseException) {
            System.err.println("Unable to upload file: " + e.details)
            throw e
        }
    }

    fun downloadFile(realFileId: String): ByteArrayOutputStream {
        try {
            val outputStream = ByteArrayOutputStream()
            service.files().get(realFileId)
                .executeMediaAndDownloadTo(outputStream)
            return outputStream
        } catch (e: GoogleJsonResponseException) {
            System.err.println("Unable to move file: " + e.details)
            throw e
        }
    }

}