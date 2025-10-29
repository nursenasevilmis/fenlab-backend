package com.nursenasevilmis.fenlab.dto.response

import com.nursenasevilmis.fenlab.model.enums.NotificationType
import java.time.LocalDateTime
//Kullanıcıya gönderilen bildirim:
data class NotificationResponseDTO(
    val id: Long,
    val type: NotificationType,
    val message: String,
    val isRead: Boolean,
    val experimentId: Long?,
    val relatedUser: UserSummaryResponseDTO?,
    val createdAt: LocalDateTime
)
