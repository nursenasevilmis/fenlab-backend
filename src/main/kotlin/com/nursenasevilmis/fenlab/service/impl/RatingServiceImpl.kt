package com.nursenasevilmis.fenlab.service.impl

import com.nursenasevilmis.fenlab.dto.request.RatingCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.RatingResponseDTO
import com.nursenasevilmis.fenlab.exception.BadRequestException
import com.nursenasevilmis.fenlab.exception.ResourceNotFoundException
import com.nursenasevilmis.fenlab.mapper.RatingMapper
import com.nursenasevilmis.fenlab.model.Rating
import com.nursenasevilmis.fenlab.repository.ExperimentRepository
import com.nursenasevilmis.fenlab.repository.RatingRepository
import com.nursenasevilmis.fenlab.repository.UserRepository
import com.nursenasevilmis.fenlab.service.RatingService
import com.nursenasevilmis.fenlab.service.UserService
import com.nursenasevilmis.fenlab.util.SecurityUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class RatingServiceImpl(
    private val ratingRepository: RatingRepository,
    private val experimentRepository: ExperimentRepository,
    private val userRepository: UserRepository,
    private val ratingMapper: RatingMapper
) : RatingService {

    @Transactional
    override fun rateExperiment(experimentId: Long, request: RatingCreateRequestDTO): RatingResponseDTO {
        val currentUserId = SecurityUtils.getCurrentUserId()

        val experiment = experimentRepository.findById(experimentId)
            .orElseThrow { ResourceNotFoundException("Deney bulunamadı: $experimentId") }

        // Kullanıcı kendi deneyini puanlayamaz (trigger kontrolü de var)
        if (experiment.user.id == currentUserId) {
            throw BadRequestException("Kendi deneyinizi puanlayamazsınız")
        }

        val user = userRepository.findById(currentUserId)
            .orElseThrow { ResourceNotFoundException("Kullanıcı bulunamadı") }

        // Mevcut puanı kontrol et
        val existingRating = ratingRepository.findByUserIdAndExperimentId(currentUserId, experimentId)

        val rating = if (existingRating != null) {
            // Güncelle
            existingRating.copy(rating = request.rating)
        } else {
            // Yeni oluştur
            Rating(
                experiment = experiment,
                user = user,
                rating = request.rating,
                createdAt = LocalDateTime.now()
            )
        }

        val savedRating = ratingRepository.save(rating)

        return ratingMapper.toRatingResponse(savedRating)
    }

    override fun getUserRating(experimentId: Long): RatingResponseDTO? {
        val currentUserId = try {
            SecurityUtils.getCurrentUserId()
        } catch (e: Exception) {
            return null
        }

        val rating = ratingRepository.findByUserIdAndExperimentId(currentUserId, experimentId)
            ?: return null

        return ratingMapper.toRatingResponse(rating)
    }

    override fun getAverageRating(experimentId: Long): Double? {
        return ratingRepository.getAverageRatingByExperimentId(experimentId)
    }
}