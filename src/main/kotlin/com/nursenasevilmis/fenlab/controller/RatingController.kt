package com.nursenasevilmis.fenlab.controller


import com.nursenasevilmis.fenlab.dto.request.RatingCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.RatingResponseDTO
import com.nursenasevilmis.fenlab.service.RatingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ratings")
@Tag(name = "Ratings", description = "Deney puanlama işlemleri")
@SecurityRequirement(name = "bearerAuth") // Swagger'da JWT token girişi için
class RatingController(
    private val ratingService: RatingService
) {

    // ==============================
    // 1️⃣ Deneyi puanlama (oluştur veya güncelle)
    // ==============================
    @PostMapping("/experiment/{experimentId}")
    @Operation(summary = "Bir deneyi puanlar veya mevcut puanı günceller")
    fun rateExperiment(
        @PathVariable experimentId: Long,
        @RequestBody request: RatingCreateRequestDTO
    ): ResponseEntity<RatingResponseDTO> {
        val response = ratingService.rateExperiment(experimentId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ==============================
    // 2️⃣ Kullanıcının kendi puanını getir
    // ==============================
    @GetMapping("/experiment/{experimentId}/me")
    @Operation(summary = "Geçerli kullanıcının bu deney için verdiği puanı getirir")
    fun getUserRating(
        @PathVariable experimentId: Long
    ): ResponseEntity<RatingResponseDTO?> {
        val response = ratingService.getUserRating(experimentId)
        return ResponseEntity.ok(response)
    }

    // ==============================
    // 3️⃣ Deneyin ortalama puanını getir
    // ==============================
    @GetMapping("/experiment/{experimentId}/average")
    @Operation(summary = "Deneyin ortalama puanını getirir")
    fun getAverageRating(
        @PathVariable experimentId: Long
    ): ResponseEntity<Double?> {
        val response = ratingService.getAverageRating(experimentId)
        return ResponseEntity.ok(response)
    }
}
