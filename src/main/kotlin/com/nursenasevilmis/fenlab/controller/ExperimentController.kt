package com.nursenasevilmis.fenlab.controller

import com.nursenasevilmis.fenlab.dto.request.ExperimentCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.request.ExperimentFilterRequestDTO
import com.nursenasevilmis.fenlab.dto.request.ExperimentUpdateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.ExperimentResponseDTO
import com.nursenasevilmis.fenlab.dto.response.ExperimentSummaryResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.model.enums.*
import com.nursenasevilmis.fenlab.service.ExperimentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/experiments")
@Tag(name = "Experiment", description = "Deney yönetimi API'leri")
class ExperimentController(
    private val experimentService: ExperimentService
) {

    @GetMapping
    @Operation(summary = "Tüm deneyleri listele")
    fun getAllExperiments(
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) subject: String?,
        @RequestParam(required = false) environment: String?,
        @RequestParam(required = false) minGradeLevel: Int?,   // eklendi
        @RequestParam(required = false) maxGradeLevel: Int?,   // eklendi
        @RequestParam(required = false) difficulty: String?,
        @RequestParam(required = false, defaultValue = "MOST_RECENT") sortType: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PaginatedResponseDTO<ExperimentSummaryResponseDTO>> {

        val filterRequest = ExperimentFilterRequestDTO(
            search       = search,
            subject      = runCatching { subject?.let { SubjectType.valueOf(it.uppercase()) } }.getOrNull(),
            environment  = runCatching { environment?.let { EnvironmentType.valueOf(it.uppercase()) } }.getOrNull(),
            minGradeLevel = minGradeLevel,
            maxGradeLevel = maxGradeLevel,
            difficulty   = runCatching { difficulty?.let { DifficultyLevel.valueOf(it.uppercase()) } }.getOrNull(),
            sortType     = runCatching { SortType.valueOf(sortType.uppercase()) }.getOrDefault(SortType.MOST_RECENT),
            page         = page,
            size         = size
        )

        return ResponseEntity.ok(experimentService.getAllExperiments(filterRequest))
    }

    @GetMapping("/{experimentId}")
    @Operation(summary = "Deney detayını getir")
    fun getExperimentById(
        @PathVariable experimentId: Long
    ): ResponseEntity<ExperimentResponseDTO> =
        ResponseEntity.ok(experimentService.getExperimentById(experimentId))

    @PostMapping
    @Operation(summary = "Yeni deney oluştur")
    fun createExperiment(
        @Valid @RequestBody request: ExperimentCreateRequestDTO
    ): ResponseEntity<ExperimentResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(experimentService.createExperiment(request))

    @PutMapping("/{experimentId}")
    @Operation(summary = "Deneyi güncelle")
    fun updateExperiment(
        @PathVariable experimentId: Long,
        @Valid @RequestBody request: ExperimentUpdateRequestDTO
    ): ResponseEntity<ExperimentResponseDTO> =
        ResponseEntity.ok(experimentService.updateExperiment(experimentId, request))

    @DeleteMapping("/{experimentId}")
    @Operation(summary = "Deneyi sil (soft delete)")
    fun deleteExperiment(@PathVariable experimentId: Long): ResponseEntity<Void> {
        experimentService.deleteExperiment(experimentId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Kullanıcının deneylerini listele")
    fun getUserExperiments(
        @PathVariable userId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PaginatedResponseDTO<ExperimentSummaryResponseDTO>> =
        ResponseEntity.ok(experimentService.getUserExperiments(userId, page, size))

    @GetMapping("/subjects")
    @Operation(summary = "Tüm ders alanlarını listele")
    fun getAllSubjects(): ResponseEntity<List<String>> =
        ResponseEntity.ok(experimentService.getAllSubjects())
}