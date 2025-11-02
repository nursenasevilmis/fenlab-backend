package com.nursenasevilmis.fenlab.dto.response

import com.nursenasevilmis.fenlab.model.enums.DifficultyLevel
import com.nursenasevilmis.fenlab.model.enums.EnvironmentType
import com.nursenasevilmis.fenlab.model.enums.SubjectType
import java.time.LocalDateTime
//Kart görünümündeki “deney kutucukları” bu veriden oluşturulur.
data class ExperimentSummaryResponseDTO(
    val id: Long,
    val user: UserSummaryResponseDTO,
    val title: String,
    val description: String,
    val gradeLevel: Int,
    val subject: SubjectType?,
    val environment: EnvironmentType?,
    val topic: String?,
    val difficulty: DifficultyLevel,
    val createdAt: LocalDateTime,

    // İlk video/thumbnail
    val thumbnailUrl: String?,
    val videoUrl: String?,

    // İstatistikler
    val favoriteCount: Long = 0,
    val averageRating: Double? = null,
    val commentCount: Long = 0,

    // Kullanıcıya özel
    val isFavoritedByCurrentUser: Boolean = false
)
