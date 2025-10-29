package com.nursenasevilmis.fenlab.mapper

import com.nursenasevilmis.fenlab.dto.response.NotificationResponseDTO
import com.nursenasevilmis.fenlab.model.Notification
import org.springframework.stereotype.Component


@Component
class NotificationMapper(
    private val userMapper: UserMapper
) {

    fun toNotificationResponse(notification: Notification): NotificationResponseDTO {
        return NotificationResponseDTO(
            id = notification.id!!,
            type = notification.type,
            message = notification.message,
            isRead = notification.isRead,
            experimentId = notification.experiment?.id,
            relatedUser = notification.relatedUser?.let { userMapper.toUserSummary(it) },
            createdAt = notification.createdAt
        )
    }
}