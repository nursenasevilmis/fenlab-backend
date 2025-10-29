package com.nursenasevilmis.fenlab.service.impl

import com.nursenasevilmis.fenlab.dto.request.AnswerCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.request.QuestionCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.dto.response.QuestionResponseDTO
import com.nursenasevilmis.fenlab.exception.ForbiddenException
import com.nursenasevilmis.fenlab.exception.ResourceNotFoundException
import com.nursenasevilmis.fenlab.mapper.QuestionMapper
import com.nursenasevilmis.fenlab.model.Question
import com.nursenasevilmis.fenlab.repository.ExperimentRepository
import com.nursenasevilmis.fenlab.repository.QuestionRepository
import com.nursenasevilmis.fenlab.repository.UserRepository
import com.nursenasevilmis.fenlab.service.NotificationService
import com.nursenasevilmis.fenlab.service.QuestionService
import com.nursenasevilmis.fenlab.service.UserService
import com.nursenasevilmis.fenlab.util.PaginationUtils
import com.nursenasevilmis.fenlab.util.SecurityUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class QuestionServiceImpl(
    private val questionRepository: QuestionRepository,
    private val experimentRepository: ExperimentRepository,
    private val userRepository: UserRepository,
    private val questionMapper: QuestionMapper,
    private val notificationService: NotificationService
) : QuestionService {

    override fun getExperimentQuestions(experimentId: Long, page: Int, size: Int): PaginatedResponseDTO<QuestionResponseDTO> {
        if (!experimentRepository.existsById(experimentId)) {
            throw ResourceNotFoundException("Deney bulunamadı: $experimentId")
        }

        val pageable = PaginationUtils.createPageable(page, size)
        val questionsPage = questionRepository.findByExperimentId(experimentId, pageable)

        val currentUserId = try {
            SecurityUtils.getCurrentUserId()
        } catch (e: Exception) {
            null
        }

        val responses = questionsPage.content.map { question ->
            questionMapper.toQuestionResponse(question, currentUserId)
        }

        return PaginatedResponseDTO(
            content = responses,
            page = questionsPage.number,
            size = questionsPage.size,
            totalElements = questionsPage.totalElements,
            totalPages = questionsPage.totalPages,
            isFirst = questionsPage.isFirst,
            isLast = questionsPage.isLast
        )
    }

    @Transactional
    override fun askQuestion(experimentId: Long, request: QuestionCreateRequestDTO): QuestionResponseDTO {
        val currentUserId = SecurityUtils.getCurrentUserId()

        val experiment = experimentRepository.findById(experimentId)
            .orElseThrow { ResourceNotFoundException("Deney bulunamadı: $experimentId") }

        val user = userRepository.findById(currentUserId)
            .orElseThrow { ResourceNotFoundException("Kullanıcı bulunamadı") }

        val question = Question(
            experiment = experiment,
            asker = user,
            questionText = request.questionText,
            createdAt = LocalDateTime.now()
        )

        val savedQuestion = questionRepository.save(question)

        // Bildirim oluştur (deney sahibine)
        if (experiment.user.id != currentUserId) {
            notificationService.createQuestionNotification(experiment, user)
        }

        return questionMapper.toQuestionResponse(savedQuestion, currentUserId)
    }

    @Transactional
    override fun answerQuestion(questionId: Long, request: AnswerCreateRequestDTO): QuestionResponseDTO {
        val currentUserId = SecurityUtils.getCurrentUserId()

        val question = questionRepository.findById(questionId)
            .orElseThrow { ResourceNotFoundException("Soru bulunamadı: $questionId") }

        // Sadece deney sahibi cevap verebilir (trigger kontrolü de var)
        if (question.experiment.user.id != currentUserId) {
            throw ForbiddenException("Sadece deney sahibi bu soruyu cevaplayabilir")
        }

        val answerer = userRepository.findById(currentUserId)
            .orElseThrow { ResourceNotFoundException("Kullanıcı bulunamadı") }

        // PostgreSQL trigger burada çalışacak
        val updatedQuestion = question.copy(
            answerText = request.answerText,
            answerer = answerer,
            answeredAt = LocalDateTime.now()
        )

        val savedQuestion = questionRepository.save(updatedQuestion)

        // Bildirim oluştur (soru soranlara)
        notificationService.createAnswerNotification(savedQuestion, answerer)

        return questionMapper.toQuestionResponse(savedQuestion, currentUserId)
    }

    @Transactional
    override fun deleteQuestion(questionId: Long) {
        val currentUserId = SecurityUtils.getCurrentUserId()

        val question = questionRepository.findById(questionId)
            .orElseThrow { ResourceNotFoundException("Soru bulunamadı: $questionId") }

        // Soru sahibi veya deney sahibi silebilir
        if (question.asker.id != currentUserId && question.experiment.user.id != currentUserId) {
            throw ForbiddenException("Bu soruyu silme yetkiniz yok")
        }

        // Soft delete
        val deletedQuestion = question.copy(isDeleted = true)
        questionRepository.save(deletedQuestion)
    }

    override fun getUnansweredQuestions(page: Int, size: Int): PaginatedResponseDTO<QuestionResponseDTO> {
        val currentUserId = SecurityUtils.getCurrentUserId()
        val pageable = PaginationUtils.createPageable(page, size)
        val questionsPage = questionRepository.findUnansweredByExperimentOwnerId(currentUserId, pageable)

        val responses = questionsPage.content.map { question ->
            questionMapper.toQuestionResponse(question, currentUserId)
        }

        return PaginatedResponseDTO(
            content = responses,
            page = questionsPage.number,
            size = questionsPage.size,
            totalElements = questionsPage.totalElements,
            totalPages = questionsPage.totalPages,
            isFirst = questionsPage.isFirst,
            isLast = questionsPage.isLast
        )
    }
}
