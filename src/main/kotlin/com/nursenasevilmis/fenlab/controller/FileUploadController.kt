package com.nursenasevilmis.fenlab.controller

import com.nursenasevilmis.fenlab.dto.response.FileUploadResponseDTO
import com.nursenasevilmis.fenlab.service.FileUploadService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "File Upload Controller", description = "Dosya yükleme, silme ve yönetimi işlemleri")
@RestController
@RequestMapping("/api/files")
class FileUploadController(
    private val fileUploadService: FileUploadService
) {

    // ================================================
    // 📸 IMAGE UPLOAD
    // ================================================
    @Operation(summary = "Görsel yükle", description = "Bir görsel dosyasını yükler ve URL döner")
    @PostMapping("/upload/image")
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<FileUploadResponseDTO> {
        val response = fileUploadService.uploadImage(file)
        return ResponseEntity.ok(response)
    }

    // ================================================
    // 🎥 VIDEO UPLOAD
    // ================================================
    @Operation(summary = "Video yükle", description = "Bir video dosyasını yükler ve URL döner")
    @PostMapping("/upload/video")
    fun uploadVideo(@RequestParam("file") file: MultipartFile): ResponseEntity<FileUploadResponseDTO> {
        val response = fileUploadService.uploadVideo(file)
        return ResponseEntity.ok(response)
    }

    // ================================================
    // 👤 PROFILE IMAGE UPLOAD
    // ================================================
    @Operation(summary = "Profil fotoğrafı yükle", description = "Kullanıcının profil fotoğrafını yükler ve URL döner")
    @PostMapping("/upload/profile")
    fun uploadProfileImage(@RequestParam("file") file: MultipartFile): ResponseEntity<FileUploadResponseDTO> {
        val response = fileUploadService.uploadProfileImage(file)
        return ResponseEntity.ok(response)
    }

    // ================================================
    // ❌ DELETE IMAGE
    // ================================================
    @Operation(summary = "Görsel sil", description = "Belirtilen URL'deki görseli siler")
    @DeleteMapping("/delete/image")
    fun deleteImage(@RequestParam("url") imageUrl: String): ResponseEntity<Void> {
        fileUploadService.deleteImage(imageUrl)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    // ================================================
    // ❌ DELETE VIDEO
    // ================================================
    @Operation(summary = "Video sil", description = "Belirtilen URL'deki videoyu siler")
    @DeleteMapping("/delete/video")
    fun deleteVideo(@RequestParam("url") videoUrl: String): ResponseEntity<Void> {
        fileUploadService.deleteVideo(videoUrl)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}