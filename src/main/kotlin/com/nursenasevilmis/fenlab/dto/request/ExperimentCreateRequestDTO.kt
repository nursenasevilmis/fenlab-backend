package com.nursenasevilmis.fenlab.dto.request

import com.nursenasevilmis.fenlab.model.enums.DifficultyLevel
import com.nursenasevilmis.fenlab.model.enums.SubjectType
import com.nursenasevilmis.fenlab.model.enums.EnvironmentType
import jakarta.validation.Valid
import jakarta.validation.constraints.*

data class ExperimentCreateRequestDTO(
    @field:NotBlank
    @field:Size(max = 255)
    val title: String,

    @field:NotBlank
    val description: String,

    @field:NotNull
    @field:Min(1)
    @field:Max(12)
    val gradeLevel: Int,

    val subject: SubjectType? = null,
    val environment: EnvironmentType? = null,
    val topic: String? = null,

    @field:NotNull
    val difficulty: DifficultyLevel,

    val expectedResult: String? = null,
    val safetyNotes: String? = null,
    val isPublished: Boolean = true,

    @field:Valid
    val materials: List<MaterialRequestDTO> = emptyList(),

    @field:Valid
    val steps: List<StepRequestDTO> = emptyList(),

    @field:Valid
    val media: List<MediaRequestDTO> = emptyList()
)
