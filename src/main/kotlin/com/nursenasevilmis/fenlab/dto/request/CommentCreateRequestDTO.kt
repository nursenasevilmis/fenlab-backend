package com.nursenasevilmis.fenlab.dto.request

import jakarta.validation.constraints.NotBlank
//Yorum ekleme istekleri için
data class CommentCreateRequestDTO(
    @field:NotBlank(message = "Yorum içeriği boş olamaz")
    val content: String
)
