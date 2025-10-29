package com.nursenasevilmis.fenlab.dto.response

import com.nursenasevilmis.fenlab.model.enums.DifficultyLevel
import java.time.LocalDateTime
//Deney detay sayfasında gösterilen verinin tamamı.
data class ExperimentResponseDTO(
    val id: Long,
    val user: UserSummaryResponseDTO,
    val title: String,
    val description: String,
    val gradeLevel: Int,
    val subject: String,
    val difficulty: DifficultyLevel,
    val expectedResult: String?,
    val safetyNotes: String?,
    val isPublished: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,

    val materials: List<MaterialResponseDTO>,
    val steps: List<StepResponseDTO>,
    val media: List<MediaResponseDTO>,

    // İstatistikler
    val favoriteCount: Long = 0,
    val averageRating: Double? = null,
    val commentCount: Long = 0,
    val questionCount: Long = 0,

    // Kullanıcıya özel bilgiler
    val isFavoritedByCurrentUser: Boolean = false,
    val currentUserRating: Int? = null
)
