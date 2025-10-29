package com.nursenasevilmis.fenlab.mapper


import com.nursenasevilmis.fenlab.dto.response.ExperimentSummaryResponseDTO
import com.nursenasevilmis.fenlab.model.Favorite
import com.nursenasevilmis.fenlab.service.ExperimentService
import org.springframework.stereotype.Component

@Component
class FavoriteMapper(
    private val experimentService: ExperimentService
) {

    fun toExperimentSummary(favorite: Favorite, currentUserId: Long?): ExperimentSummaryResponseDTO {
        return experimentService.mapToExperimentSummary(favorite.experiment, currentUserId)
    }
}
