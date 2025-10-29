package com.nursenasevilmis.fenlab.dto.response
//Kullanıcının genel deney istatistikleri:Profil istatistik grafikleri için.
data class ExperimentStatsResponseDTO(
    val totalExperiments: Long,
    val totalFavorites: Long,
    val totalComments: Long,
    val totalQuestions: Long,
    val averageRating: Double?
)
