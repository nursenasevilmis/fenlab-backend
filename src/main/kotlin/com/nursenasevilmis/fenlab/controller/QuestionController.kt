package com.nursenasevilmis.fenlab.controller


import com.nursenasevilmis.fenlab.dto.request.AnswerCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.request.QuestionCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.dto.response.QuestionResponseDTO
import com.nursenasevilmis.fenlab.service.QuestionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/questions")
@Tag(name = "Questions", description = "Soru sorma, cevaplama ve listeleme işlemleri")
class QuestionController(
    private val questionService: QuestionService
) {

    // ==============================
    // 1️⃣ Belirli bir deneyin tüm sorularını listele
    // ==============================
    @GetMapping("/experiment/{experimentId}")
    @Operation(summary = "Belirli bir deneyin sorularını getirir")
    fun getQuestionsByExperiment(
        @PathVariable experimentId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponseDTO<QuestionResponseDTO>> {
        val response = questionService.getExperimentQuestions(experimentId, page, size)
        return ResponseEntity.ok(response)
    }

    // ==============================
    // 2️⃣ Soru sorma (öğrenci tarafından)
    // ==============================
    @PostMapping("/experiment/{experimentId}")
    @Operation(summary = "Bir deneye yeni soru ekler (öğrenciler için)")
    fun askQuestion(
        @PathVariable experimentId: Long,
        @RequestBody request: QuestionCreateRequestDTO
    ): ResponseEntity<QuestionResponseDTO> {
        val response = questionService.askQuestion(experimentId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ==============================
    // 3️⃣ Soruyu cevaplama (deney sahibi öğretmen tarafından)
    // ==============================
    @PostMapping("/{questionId}/answer")
    @Operation(summary = "Bir soruya cevap ekler (deney sahibi tarafından)")
    fun answerQuestion(
        @PathVariable questionId: Long,
        @RequestBody request: AnswerCreateRequestDTO
    ): ResponseEntity<QuestionResponseDTO> {
        val response = questionService.answerQuestion(questionId, request)
        return ResponseEntity.ok(response)
    }

    // ==============================
    // 4️⃣ Soruyu silme (soru sahibi veya deney sahibi)
    // ==============================
    @DeleteMapping("/{questionId}")
    @Operation(summary = "Bir soruyu siler (soru sahibi veya deney sahibi tarafından)")
    fun deleteQuestion(
        @PathVariable questionId: Long
    ): ResponseEntity<Void> {
        questionService.deleteQuestion(questionId)
        return ResponseEntity.noContent().build()
    }

    // ==============================
    // 5️⃣ Cevaplanmamış soruları getir (öğretmen paneli için)
    // ==============================
    @GetMapping("/unanswered")
    @Operation(summary = "Kullanıcının deneylerindeki cevaplanmamış soruları getirir")
    fun getUnansweredQuestions(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponseDTO<QuestionResponseDTO>> {
        val response = questionService.getUnansweredQuestions(page, size)
        return ResponseEntity.ok(response)
    }
}
