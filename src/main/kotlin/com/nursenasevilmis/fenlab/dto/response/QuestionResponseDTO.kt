package com.nursenasevilmis.fenlab.dto.response

import java.time.LocalDateTime
//Deney altındaki soru-cevap verisi:
data class QuestionResponseDTO(
    val id: Long,
    val asker: UserSummaryResponseDTO,
    val questionText: String,
    val answerText: String?,
    val answerer: UserSummaryResponseDTO?,
    val createdAt: LocalDateTime,
    val answeredAt: LocalDateTime?,
    val isAnswered: Boolean = false,
    val canAnswer: Boolean = false
)
