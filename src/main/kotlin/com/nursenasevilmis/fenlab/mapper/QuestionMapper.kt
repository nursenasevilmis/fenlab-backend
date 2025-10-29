package com.nursenasevilmis.fenlab.mapper

import com.nursenasevilmis.fenlab.dto.response.QuestionResponseDTO
import com.nursenasevilmis.fenlab.model.Question
import org.springframework.stereotype.Component


@Component
class QuestionMapper(
    private val userMapper: UserMapper
) {

    fun toQuestionResponse(question: Question, currentUserId: Long?): QuestionResponseDTO {
        val canAnswer = question.experiment.user.id == currentUserId && question.answerText == null

        return QuestionResponseDTO(
            id = question.id!!,
            asker = userMapper.toUserSummary(question.asker),
            questionText = question.questionText,
            answerText = question.answerText,
            answerer = question.answerer?.let { userMapper.toUserSummary(it) },
            createdAt = question.createdAt,
            answeredAt = question.answeredAt,
            isAnswered = question.answerText != null,
            canAnswer = canAnswer
        )
    }
}