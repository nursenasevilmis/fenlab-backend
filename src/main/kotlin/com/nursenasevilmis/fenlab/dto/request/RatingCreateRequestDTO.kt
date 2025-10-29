package com.nursenasevilmis.fenlab.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
//Deneyleri oylamak için.
data class RatingCreateRequestDTO(
    @field:NotNull(message = "Puan belirtilmelidir")
    @field:Min(value = 1, message = "Puan en az 1 olmalıdır")
    @field:Max(value = 5, message = "Puan en fazla 5 olabilir")
    val rating: Int
)
