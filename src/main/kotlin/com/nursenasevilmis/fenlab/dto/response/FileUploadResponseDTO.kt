package com.nursenasevilmis.fenlab.dto.response

data class FileUploadResponseDTO(
    val fileName: String,
    val fileUrl: String,
    val fileSize: Long,
    val contentType: String
)
