package com.nursenasevilmis.fenlab.dto.request

import com.nursenasevilmis.fenlab.model.enums.*

data class ExperimentFilterRequestDTO(
    val search: String? = null,
    val subject: SubjectType? = null,
    val environment: EnvironmentType? = null,
    val gradeLevel: Int? = null,
    val difficulty: DifficultyLevel? = null,
    val sortType: SortType = SortType.MOST_RECENT,
    val page: Int = 0,
    val size: Int = 20
)
