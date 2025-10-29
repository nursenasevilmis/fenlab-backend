package com.nursenasevilmis.fenlab.dto.response

import java.time.LocalDateTime
//Bir yorumun bilgilerini içerir:
data class CommentResponseDTO(
    val id: Long,
    val user: UserSummaryResponseDTO,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val isOwner: Boolean = false
)
