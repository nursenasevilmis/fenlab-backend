package com.nursenasevilmis.fenlab.service

import com.nursenasevilmis.fenlab.dto.request.RatingCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.RatingResponseDTO

interface RatingService {
    fun rateExperiment(experimentId: Long, request: RatingCreateRequestDTO): RatingResponseDTO
    fun getUserRating(experimentId: Long): RatingResponseDTO?
    fun getAverageRating(experimentId: Long): Double?
}