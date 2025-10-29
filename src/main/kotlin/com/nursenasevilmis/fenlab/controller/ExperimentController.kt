package com.nursenasevilmis.fenlab.controller

import com.nursenasevilmis.fenlab.dto.request.ExperimentCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.request.ExperimentFilterRequestDTO
import com.nursenasevilmis.fenlab.dto.request.ExperimentUpdateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.ExperimentResponseDTO
import com.nursenasevilmis.fenlab.dto.response.ExperimentSummaryResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.service.ExperimentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/experiments")
@Tag(name = "Experiment", description = "Deney yönetimi API'leri")
class ExperimentController(
    private val experimentService: ExperimentService
) {

    @GetMapping
    @Operation(
        summary = "Tüm deneyleri listele",
        description = "Filtreleme, arama ve sıralama seçenekleriyle deneyleri listeler"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Deneyler başarıyla listelendi",
                content = [Content(schema = Schema(implementation = PaginatedResponseDTO::class))]
            )
        ]
    )
    fun getAllExperiments(
        @Parameter(description = "Arama metni")
        @RequestParam(required = false) search: String?,

        @Parameter(description = "Ders alanı filtresi")
        @RequestParam(required = false) subject: String?,

        @Parameter(description = "Sınıf seviyesi filtresi")
        @RequestParam(required = false) gradeLevel: Int?,

        @Parameter(description = "Zorluk seviyesi filtresi")
        @RequestParam(required = false) difficulty: String?,

        @Parameter(description = "Sıralama türü (MOST_RECENT, OLDEST, HIGHEST_RATED, MOST_FAVORITED)")
        @RequestParam(required = false, defaultValue = "MOST_RECENT") sortType: String,

        @Parameter(description = "Sayfa numarası")
        @RequestParam(defaultValue = "0") page: Int,

        @Parameter(description = "Sayfa boyutu")
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PaginatedResponseDTO<ExperimentSummaryResponseDTO>> {
        val filterRequest = ExperimentFilterRequestDTO(
            search = search,
            subject = subject,
            gradeLevel = gradeLevel,
            difficulty = difficulty?.let { com.nursenasevilmis.fenlab.model.enums.DifficultyLevel.valueOf(it) },
            sortType = com.nursenasevilmis.fenlab.model.enums.SortType.valueOf(sortType),
            page = page,
            size = size
        )

        val result = experimentService.getAllExperiments(filterRequest)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{experimentId}")
    @Operation(
        summary = "Deney detayını getir",
        description = "Belirtilen ID'ye sahip deneyin tüm detaylarını getirir"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Deney başarıyla getirildi",
                content = [Content(schema = Schema(implementation = ExperimentResponseDTO::class))]
            ),
            ApiResponse(responseCode = "404", description = "Deney bulunamadı"),
            ApiResponse(responseCode = "403", description = "Bu deneye erişim izniniz yok")
        ]
    )
    fun getExperimentById(
        @Parameter(description = "Deney ID'si", required = true)
        @PathVariable experimentId: Long
    ): ResponseEntity<ExperimentResponseDTO> {
        val experiment = experimentService.getExperimentById(experimentId)
        return ResponseEntity.ok(experiment)
    }

    @PostMapping
    @Operation(
        summary = "Yeni deney oluştur",
        description = "Yeni deney oluşturur (Test mode - authentication disabled)"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Deney başarıyla oluşturuldu",
                content = [Content(schema = Schema(implementation = ExperimentResponseDTO::class))]
            ),
            ApiResponse(responseCode = "400", description = "Geçersiz istek verisi")
        ]
    )
    fun createExperiment(
        @Valid @RequestBody request: ExperimentCreateRequestDTO
    ): ResponseEntity<ExperimentResponseDTO> {
        val experiment = experimentService.createExperiment(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(experiment)
    }

    @PutMapping("/{experimentId}")
    @Operation(
        summary = "Deneyi güncelle",
        description = "Deneyi günceller (Test mode - authentication disabled)"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Deney başarıyla güncellendi",
                content = [Content(schema = Schema(implementation = ExperimentResponseDTO::class))]
            ),
            ApiResponse(responseCode = "400", description = "Geçersiz istek verisi"),
            ApiResponse(responseCode = "404", description = "Deney bulunamadı")
        ]
    )
    fun updateExperiment(
        @Parameter(description = "Deney ID'si", required = true)
        @PathVariable experimentId: Long,

        @Valid @RequestBody request: ExperimentUpdateRequestDTO
    ): ResponseEntity<ExperimentResponseDTO> {
        val experiment = experimentService.updateExperiment(experimentId, request)
        return ResponseEntity.ok(experiment)
    }

    @DeleteMapping("/{experimentId}")
    @Operation(
        summary = "Deneyi sil",
        description = "Deneyi siler - soft delete (Test mode - authentication disabled)"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Deney başarıyla silindi"),
            ApiResponse(responseCode = "404", description = "Deney bulunamadı")
        ]
    )
    fun deleteExperiment(
        @Parameter(description = "Deney ID'si", required = true)
        @PathVariable experimentId: Long
    ): ResponseEntity<Void> {
        experimentService.deleteExperiment(experimentId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Kullanıcının deneylerini listele",
        description = "Belirtilen kullanıcının tüm deneylerini listeler"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Kullanıcı deneyleri başarıyla listelendi",
                content = [Content(schema = Schema(implementation = PaginatedResponseDTO::class))]
            ),
            ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
        ]
    )
    fun getUserExperiments(
        @Parameter(description = "Kullanıcı ID'si", required = true)
        @PathVariable userId: Long,

        @Parameter(description = "Sayfa numarası")
        @RequestParam(defaultValue = "0") page: Int,

        @Parameter(description = "Sayfa boyutu")
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PaginatedResponseDTO<ExperimentSummaryResponseDTO>> {
        val experiments = experimentService.getUserExperiments(userId, page, size)
        return ResponseEntity.ok(experiments)
    }

    @GetMapping("/subjects")
    @Operation(
        summary = "Tüm ders alanlarını listele",
        description = "Sistemde kayıtlı tüm ders alanlarının listesini döndürür"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Ders alanları başarıyla listelendi"
            )
        ]
    )
    fun getAllSubjects(): ResponseEntity<List<String>> {
        val subjects = experimentService.getAllSubjects()
        return ResponseEntity.ok(subjects)
    }
}