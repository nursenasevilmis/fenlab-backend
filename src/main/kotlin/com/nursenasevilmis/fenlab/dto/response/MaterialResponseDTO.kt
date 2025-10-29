package com.nursenasevilmis.fenlab.dto.response
//Kaydedilen malzeme bilgisini döndürür.
data class MaterialResponseDTO(
    val id: Long,
    val materialName: String,
    val quantity: String
)
