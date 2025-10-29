package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.Notification
import com.nursenasevilmis.fenlab.model.enums.NotificationType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {

    fun findByUserId(userId: Long, pageable: Pageable): Page<Notification>

    fun findByUserIdAndIsReadFalse(userId: Long, pageable: Pageable): Page<Notification>

    fun countByUserIdAndIsReadFalse(userId: Long): Long

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId")
    fun markAllAsReadByUserId(userId: Long)

    fun deleteByType(type: NotificationType)

}
