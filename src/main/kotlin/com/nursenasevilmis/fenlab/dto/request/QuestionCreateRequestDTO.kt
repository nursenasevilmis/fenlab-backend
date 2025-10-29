package com.nursenasevilmis.fenlab.dto.request

import jakarta.validation.constraints.NotBlank
//Bir kullanıcı deney sayfasında soru sorarsa bu DTO kullanılır.
data class QuestionCreateRequestDTO(
    @field:NotBlank(message = "Soru metni boş olamaz")
    val questionText: String
)
