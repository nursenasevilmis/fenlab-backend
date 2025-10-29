package com.nursenasevilmis.fenlab.mapper

import com.nursenasevilmis.fenlab.dto.response.RatingResponseDTO
import com.nursenasevilmis.fenlab.model.Rating
import org.springframework.stereotype.Component

@Component
class RatingMapper(
    private val userMapper: UserMapper
) {

    fun toRatingResponse(rating: Rating): RatingResponseDTO {
        return RatingResponseDTO(
            id = rating.id!!,
            user = userMapper.toUserSummary(rating.user),
            rating = rating.rating,
            createdAt = rating.createdAt
        )
    }
}