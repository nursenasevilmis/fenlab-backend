package com.nursenasevilmis.fenlab.dto.response

import com.nursenasevilmis.fenlab.model.enums.MediaType
//Kaydedilen medya bilgisini döndürür.
data class MediaResponseDTO(
    val id: Long,
    val mediaType: MediaType,
    val mediaUrl: String,
    val mediaOrder: Int
)
