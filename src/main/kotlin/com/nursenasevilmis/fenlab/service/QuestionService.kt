package com.nursenasevilmis.fenlab.service

import com.nursenasevilmis.fenlab.dto.request.AnswerCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.request.QuestionCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.dto.response.QuestionResponseDTO

interface QuestionService {
    fun getExperimentQuestions(experimentId: Long, page: Int, size: Int): PaginatedResponseDTO<QuestionResponseDTO>
    fun askQuestion(experimentId: Long, request: QuestionCreateRequestDTO): QuestionResponseDTO
    fun answerQuestion(questionId: Long, request: AnswerCreateRequestDTO): QuestionResponseDTO
    fun deleteQuestion(questionId: Long)
    fun getUnansweredQuestions(page: Int, size: Int): PaginatedResponseDTO<QuestionResponseDTO>
}