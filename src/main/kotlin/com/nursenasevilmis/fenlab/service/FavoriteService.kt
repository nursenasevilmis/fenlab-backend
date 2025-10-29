package com.nursenasevilmis.fenlab.service

import com.nursenasevilmis.fenlab.dto.response.ExperimentSummaryResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO


interface FavoriteService {
    fun getUserFavorites(page: Int, size: Int): PaginatedResponseDTO<ExperimentSummaryResponseDTO>
    fun addToFavorites(experimentId: Long): String
    fun removeFromFavorites(experimentId: Long): String
    fun isFavorited(experimentId: Long): Boolean
}