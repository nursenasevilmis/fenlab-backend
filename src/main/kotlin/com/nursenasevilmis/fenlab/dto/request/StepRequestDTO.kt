package com.nursenasevilmis.fenlab.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
//Bir deney adımını temsil eder:
data class StepRequestDTO(
    @field:NotNull(message = "Adım sırası belirtilmelidir")
    val stepOrder: Int,

    @field:NotBlank(message = "Adım açıklaması boş olamaz")
    val stepText: String
)
