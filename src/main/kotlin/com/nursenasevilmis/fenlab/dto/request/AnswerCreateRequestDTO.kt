package com.nursenasevilmis.fenlab.dto.request

import jakarta.validation.constraints.NotBlank
//Deneyi paylaşan kişi soruya yanıt verdiğinde.
data class AnswerCreateRequestDTO(
    @field:NotBlank(message = "Cevap metni boş olamaz")
    val answerText: String
)
