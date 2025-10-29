package com.nursenasevilmis.fenlab.service.impl

import com.nursenasevilmis.fenlab.dto.response.NotificationResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.exception.ForbiddenException
import com.nursenasevilmis.fenlab.exception.ResourceNotFoundException
import com.nursenasevilmis.fenlab.mapper.NotificationMapper
import com.nursenasevilmis.fenlab.model.Experiment
import com.nursenasevilmis.fenlab.model.Notification
import com.nursenasevilmis.fenlab.model.Question
import com.nursenasevilmis.fenlab.model.User
import com.nursenasevilmis.fenlab.model.enums.NotificationType
import com.nursenasevilmis.fenlab.repository.NotificationRepository
import com.nursenasevilmis.fenlab.service.NotificationService
import com.nursenasevilmis.fenlab.util.PaginationUtils
import com.nursenasevilmis.fenlab.util.SecurityUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val notificationMapper: NotificationMapper
) : NotificationService {

    override fun getUserNotifications(page: Int, size: Int): PaginatedResponseDTO<NotificationResponseDTO> {
        val currentUserId = SecurityUtils.getCurrentUserId()
        val pageable = PaginationUtils.createPageable(page, size)
        val notificationsPage = notificationRepository.findByUserId(currentUserId, pageable)

        val responses = notificationsPage.content.map(notificationMapper::toNotificationResponse)

        return PaginatedResponseDTO(
            content = responses,
            page = notificationsPage.number,
            size = notificationsPage.size,
            totalElements = notificationsPage.totalElements,
            totalPages = notificationsPage.totalPages,
            isFirst = notificationsPage.isFirst,
            isLast = notificationsPage.isLast
        )
    }

    override fun getUnreadNotifications(page: Int, size: Int): PaginatedResponseDTO<NotificationResponseDTO> {
        val currentUserId = SecurityUtils.getCurrentUserId()
        val pageable = PaginationUtils.createPageable(page, size)
        val notificationsPage = notificationRepository.findByUserIdAndIsReadFalse(currentUserId, pageable)

        val responses = notificationsPage.content.map(notificationMapper::toNotificationResponse)

        return PaginatedResponseDTO(
            content = responses,
            page = notificationsPage.number,
            size = notificationsPage.size,
            totalElements = notificationsPage.totalElements,
            totalPages = notificationsPage.totalPages,
            isFirst = notificationsPage.isFirst,
            isLast = notificationsPage.isLast
        )
    }

    @Transactional
    override fun markAsRead(notificationId: Long) {
        val currentUserId = SecurityUtils.getCurrentUserId()

        val notification = notificationRepository.findById(notificationId)
            .orElseThrow { ResourceNotFoundException("Bildirim bulunamadı: $notificationId") }

        if (notification.user.id != currentUserId) {
            throw ForbiddenException("Bu bildirimi işleme yetkiniz yok")
        }

        val updatedNotification = notification.copy(isRead = true)
        notificationRepository.save(updatedNotification)
    }

    @Transactional
    override fun markAllAsRead() {
        val currentUserId = SecurityUtils.getCurrentUserId()
        notificationRepository.markAllAsReadByUserId(currentUserId)
    }

    override fun getUnreadCount(): Long {
        val currentUserId = SecurityUtils.getCurrentUserId()
        return notificationRepository.countByUserIdAndIsReadFalse(currentUserId)
    }

    // --- Bildirim oluşturma metodları ---
    @Transactional
    override fun createCommentNotification(experiment: Experiment, commenter: User) {
        val notification = Notification(
            user = experiment.user,
            type = NotificationType.COMMENT,
            message = "${commenter.fullName} deneyinize yorum yaptı: ${experiment.title}",
            experiment = experiment,
            relatedUser = commenter,
            createdAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)
    }

    @Transactional
    override fun createQuestionNotification(experiment: Experiment, asker: User) {
        val notification = Notification(
            user = experiment.user,
            type = NotificationType.QUESTION,
            message = "${asker.fullName} deneyinize soru sordu: ${experiment.title}",
            experiment = experiment,
            relatedUser = asker,
            createdAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)
    }

    @Transactional
    override fun createAnswerNotification(question: Question, answerer: User) {
        val notification = Notification(
            user = question.asker,
            type = NotificationType.ANSWER,
            message = "${answerer.fullName} sorunuzu cevapladı: ${question.experiment.title}",
            experiment = question.experiment,
            relatedUser = answerer,
            createdAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)
    }

    @Transactional
    override fun createFavoriteNotification(experiment: Experiment, user: User) {
        val notification = Notification(
            user = experiment.user,
            type = NotificationType.FAVORITE,
            message = "${user.fullName} deneyinizi favorilerine ekledi: ${experiment.title}",
            experiment = experiment,
            relatedUser = user,
            createdAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)
    }
}
