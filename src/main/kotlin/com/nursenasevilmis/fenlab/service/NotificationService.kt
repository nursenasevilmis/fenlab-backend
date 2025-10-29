package com.nursenasevilmis.fenlab.service

import com.nursenasevilmis.fenlab.dto.response.NotificationResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.model.Experiment
import com.nursenasevilmis.fenlab.model.Question
import com.nursenasevilmis.fenlab.model.User

interface NotificationService {
    fun getUserNotifications(page: Int, size: Int): PaginatedResponseDTO<NotificationResponseDTO>
    fun getUnreadNotifications(page: Int, size: Int): PaginatedResponseDTO<NotificationResponseDTO>
    fun markAsRead(notificationId: Long)
    fun markAllAsRead()
    fun getUnreadCount(): Long
    fun createCommentNotification(experiment: Experiment, commenter: User)
    fun createQuestionNotification(experiment: Experiment, asker: User)
    fun createAnswerNotification(question: Question, answerer: User)
    fun createFavoriteNotification(experiment: Experiment, user: User)
}