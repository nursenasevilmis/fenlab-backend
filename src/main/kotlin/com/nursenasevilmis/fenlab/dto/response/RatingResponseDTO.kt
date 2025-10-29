package com.nursenasevilmis.fenlab.dto.response

import java.time.LocalDateTime
//Deneye verilen puan bilgisi:
data class RatingResponseDTO(
    val id: Long,
    val user: UserSummaryResponseDTO,
    val rating: Int,
    val createdAt: LocalDateTime
)
