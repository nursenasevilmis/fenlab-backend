package com.nursenasevilmis.fenlab.dto.request

import com.nursenasevilmis.fenlab.model.enums.MediaType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
//Deneye ait fotoğraf veya video bilgisini taşır:
data class MediaRequestDTO(
    @field:NotNull(message = "Medya tipi belirtilmelidir")
    val mediaType: MediaType,

    @field:NotBlank(message = "Medya URL'si boş olamaz")
    val mediaUrl: String,

    val mediaOrder: Int = 0
)
