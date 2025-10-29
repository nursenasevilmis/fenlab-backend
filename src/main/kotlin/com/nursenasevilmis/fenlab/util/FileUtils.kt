package com.nursenasevilmis.fenlab.util

import com.nursenasevilmis.fenlab.exception.FileUploadException
import org.springframework.web.multipart.MultipartFile

object FileUtils {

    private val ALLOWED_VIDEO_EXTENSIONS = setOf("mp4", "avi", "mov", "wmv", "flv", "mkv")
    private val ALLOWED_IMAGE_EXTENSIONS = setOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
    private val ALLOWED_VIDEO_MIME_TYPES = setOf(
        "video/mp4", "video/x-msvideo", "video/quicktime",
        "video/x-ms-wmv", "video/x-flv", "video/x-matroska"
    )
    private val ALLOWED_IMAGE_MIME_TYPES = setOf(
        "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp"
    )

    private const val MAX_VIDEO_SIZE = 100 * 1024 * 1024L // 100MB
    private const val MAX_IMAGE_SIZE = 10 * 1024 * 1024L // 10MB

    /**
     * Video dosyasını doğrular
     */
    fun validateVideoFile(file: MultipartFile) {
        if (file.isEmpty) {
            throw FileUploadException("Video dosyası boş olamaz")
        }

        if (file.size > MAX_VIDEO_SIZE) {
            throw FileUploadException("Video boyutu maksimum ${MAX_VIDEO_SIZE / (1024 * 1024)}MB olabilir")
        }

        val extension = getFileExtension(file.originalFilename ?: "")
        if (!ALLOWED_VIDEO_EXTENSIONS.contains(extension.lowercase())) {
            throw FileUploadException("Geçersiz video formatı. İzin verilen formatlar: ${ALLOWED_VIDEO_EXTENSIONS.joinToString()}")
        }

        val mimeType = file.contentType
        if (mimeType != null && !ALLOWED_VIDEO_MIME_TYPES.contains(mimeType)) {
            throw FileUploadException("Geçersiz video MIME tipi: $mimeType")
        }
    }

    /**
     * Resim dosyasını doğrular
     */
    fun validateImageFile(file: MultipartFile) {
        if (file.isEmpty) {
            throw FileUploadException("Resim dosyası boş olamaz")
        }

        if (file.size > MAX_IMAGE_SIZE) {
            throw FileUploadException("Resim boyutu maksimum ${MAX_IMAGE_SIZE / (1024 * 1024)}MB olabilir")
        }

        val extension = getFileExtension(file.originalFilename ?: "")
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension.lowercase())) {
            throw FileUploadException("Geçersiz resim formatı. İzin verilen formatlar: ${ALLOWED_IMAGE_EXTENSIONS.joinToString()}")
        }

        val mimeType = file.contentType
        if (mimeType != null && !ALLOWED_IMAGE_MIME_TYPES.contains(mimeType)) {
            throw FileUploadException("Geçersiz resim MIME tipi: $mimeType")
        }
    }

    /**
     * Dosya uzantısını döndürür
     */
    fun getFileExtension(filename: String): String {
        val lastDotIndex = filename.lastIndexOf('.')
        return if (lastDotIndex > 0) {
            filename.substring(lastDotIndex + 1)
        } else {
            ""
        }
    }

    /**
     * Dosya boyutunu human-readable formata çevirir
     */
    fun formatFileSize(size: Long): String {
        val kb = size / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0

        return when {
            gb >= 1 -> String.format("%.2f GB", gb)
            mb >= 1 -> String.format("%.2f MB", mb)
            kb >= 1 -> String.format("%.2f KB", kb)
            else -> "$size bytes"
        }
    }
}