package com.nursenasevilmis.fenlab.dto.request

import com.nursenasevilmis.fenlab.model.enums.DifficultyLevel
import com.nursenasevilmis.fenlab.model.enums.SortType
//Deneyleri filtreleme/listeleme için kullanılır:
data class ExperimentFilterRequestDTO(
    val search: String? = null,
    val subject: String? = null,
    val gradeLevel: Int? = null,
    val difficulty: DifficultyLevel? = null,
    val sortType: SortType = SortType.MOST_RECENT,
    val page: Int = 0,
    val size: Int = 20
)
