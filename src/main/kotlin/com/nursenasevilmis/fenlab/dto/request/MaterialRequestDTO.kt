package com.nursenasevilmis.fenlab.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
//Bir deneyde kullanılacak malzemeyi temsil eder:
data class MaterialRequestDTO(
    @field:NotBlank(message = "Malzeme adı boş olamaz")
    @field:Size(max = 255)
    val materialName: String,

    @field:NotBlank(message = "Miktar boş olamaz")
    @field:Size(max = 50)
    val quantity: String
)
