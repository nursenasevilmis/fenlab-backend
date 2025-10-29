package com.nursenasevilmis.fenlab.service.impl

import com.nursenasevilmis.fenlab.dto.response.ExperimentSummaryResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.exception.BadRequestException
import com.nursenasevilmis.fenlab.exception.ResourceNotFoundException
import com.nursenasevilmis.fenlab.mapper.FavoriteMapper
import com.nursenasevilmis.fenlab.model.Favorite
import com.nursenasevilmis.fenlab.repository.ExperimentRepository
import com.nursenasevilmis.fenlab.repository.FavoriteRepository
import com.nursenasevilmis.fenlab.repository.UserRepository
import com.nursenasevilmis.fenlab.service.FavoriteService
import com.nursenasevilmis.fenlab.util.PaginationUtils
import com.nursenasevilmis.fenlab.util.SecurityUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FavoriteServiceImpl(
    private val favoriteRepository: FavoriteRepository,
    private val experimentRepository: ExperimentRepository,
    private val userRepository: UserRepository,
    private val favoriteMapper: FavoriteMapper
) : FavoriteService {

    override fun getUserFavorites(page: Int, size: Int): PaginatedResponseDTO<ExperimentSummaryResponseDTO> {
        val currentUserId = SecurityUtils.getCurrentUserId()
        val pageable = PaginationUtils.createPageable(page, size)
        val favoritesPage = favoriteRepository.findByUserId(currentUserId, pageable)

        val summaries = favoritesPage.content.map { favorite ->
            favoriteMapper.toExperimentSummary(favorite, currentUserId)
        }

        return PaginatedResponseDTO(
            content = summaries,
            page = favoritesPage.number,
            size = favoritesPage.size,
            totalElements = favoritesPage.totalElements,
            totalPages = favoritesPage.totalPages,
            isFirst = favoritesPage.isFirst,
            isLast = favoritesPage.isLast
        )
    }

    @Transactional
    override fun addToFavorites(experimentId: Long): String {
        val currentUserId = SecurityUtils.getCurrentUserId()

        val experiment = experimentRepository.findById(experimentId)
            .orElseThrow { ResourceNotFoundException("Deney bulunamadı: $experimentId") }

        if (favoriteRepository.existsByUserIdAndExperimentId(currentUserId, experimentId)) {
            throw BadRequestException("Bu deney zaten favorilerinizde")
        }

        val user = userRepository.findById(currentUserId)
            .orElseThrow { ResourceNotFoundException("Kullanıcı bulunamadı") }

        val favorite = Favorite(
            user = user,
            experiment = experiment,
            createdAt = LocalDateTime.now()
        )

        favoriteRepository.save(favorite)
        return "Deney favorilere eklendi"
    }

    @Transactional
    override fun removeFromFavorites(experimentId: Long): String {
        val currentUserId = SecurityUtils.getCurrentUserId()

        if (!favoriteRepository.existsByUserIdAndExperimentId(currentUserId, experimentId)) {
            throw ResourceNotFoundException("Bu deney favorilerinizde değil")
        }

        favoriteRepository.deleteByUserIdAndExperimentId(currentUserId, experimentId)
        return "Deney favorilerden çıkarıldı"
    }

    override fun isFavorited(experimentId: Long): Boolean {
        return try {
            val currentUserId = SecurityUtils.getCurrentUserId()
            favoriteRepository.existsByUserIdAndExperimentId(currentUserId, experimentId)
        } catch (e: Exception) {
            false
        }
    }
}
