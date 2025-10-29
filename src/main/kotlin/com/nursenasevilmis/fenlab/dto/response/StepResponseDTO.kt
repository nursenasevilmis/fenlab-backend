package com.nursenasevilmis.fenlab.dto.response
//Kaydedilen adım bilgisini döndürür.
data class StepResponseDTO (
    val id: Long,
    val stepOrder: Int,
    val stepText: String
)