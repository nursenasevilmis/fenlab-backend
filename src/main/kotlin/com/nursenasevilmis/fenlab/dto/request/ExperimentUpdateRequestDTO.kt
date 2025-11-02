package com.nursenasevilmis.fenlab.dto.request

import com.nursenasevilmis.fenlab.model.enums.DifficultyLevel
import com.nursenasevilmis.fenlab.model.enums.SubjectType
import com.nursenasevilmis.fenlab.model.enums.EnvironmentType
import jakarta.validation.Valid
import jakarta.validation.constraints.*

data class ExperimentUpdateRequestDTO(
    @field:Size(max = 255)
    val title: String? = null,

    val description: String? = null,

    @field:Min(1)
    @field:Max(12)
    val gradeLevel: Int? = null,

    val subject: SubjectType? = null,
    val environment: EnvironmentType? = null,
    val topic: String? = null,

    val difficulty: DifficultyLevel? = null,
    val expectedResult: String? = null,
    val safetyNotes: String? = null,
    val isPublished: Boolean? = null,

    @field:Valid
    val materials: List<MaterialRequestDTO>? = null,

    @field:Valid
    val steps: List<StepRequestDTO>? = null,

    @field:Valid
    val media: List<MediaRequestDTO>? = null
)
