package com.nursenasevilmis.fenlab.dto.request

import jakarta.validation.constraints.NotBlank
//Yorum düzenleme istekleri için
data class CommentUpdateRequestDTO(
    @field:NotBlank(message = "Yorum içeriği boş olamaz")
    val content: String
)
