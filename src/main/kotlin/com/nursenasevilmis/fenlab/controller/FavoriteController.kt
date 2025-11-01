package com.nursenasevilmis.fenlab.controller


import com.nursenasevilmis.fenlab.dto.response.ExperimentSummaryResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.service.FavoriteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorite Controller", description = "Favori deney işlemlerini yönetir.")
class FavoriteController(
    private val favoriteService: FavoriteService
) {

    @Operation(summary = "Kullanıcının favori deneylerini getir")
    @GetMapping
    fun getUserFavorites(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponseDTO<ExperimentSummaryResponseDTO>> {
        val favorites = favoriteService.getUserFavorites(page, size)
        return ResponseEntity.ok(favorites)
    }

    @Operation(summary = "Bir deneyi favorilere ekle")
    @PostMapping("/{experimentId}")
    fun addToFavorites(
        @PathVariable experimentId: Long
    ): ResponseEntity<Map<String, String>> {
        val message = favoriteService.addToFavorites(experimentId)
        return ResponseEntity.ok(mapOf("message" to message))
    }

    @Operation(summary = "Bir deneyi favorilerden çıkar")
    @DeleteMapping("/{experimentId}")
    fun removeFromFavorites(
        @PathVariable experimentId: Long
    ): ResponseEntity<Map<String, String>> {
        val message = favoriteService.removeFromFavorites(experimentId)
        return ResponseEntity.ok(mapOf("message" to message))
    }

    @Operation(summary = "Belirli bir deney favorilere eklenmiş mi kontrol et")
    @GetMapping("/{experimentId}/is-favorited")
    fun isFavorited(
        @PathVariable experimentId: Long
    ): ResponseEntity<Map<String, Boolean>> {
        val isFavorited = favoriteService.isFavorited(experimentId)
        return ResponseEntity.ok(mapOf("isFavorited" to isFavorited))
    }
}
