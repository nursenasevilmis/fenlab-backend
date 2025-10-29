package com.nursenasevilmis.fenlab.dto.response

import java.time.LocalDateTime
//Hata durumlarında dönen yapı:Exception Handler tarafından oluşturulur.
data class ErrorResponseDTO(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)
