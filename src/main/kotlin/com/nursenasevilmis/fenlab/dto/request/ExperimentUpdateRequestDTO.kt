package com.nursenasevilmis.fenlab.dto.request

import com.nursenasevilmis.fenlab.model.enums.DifficultyLevel
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
//Var olan bir deneyi düzenlemek için kullanılır.
data class ExperimentUpdateRequestDTO(
    @field:Size(max = 255)
    val title: String? = null,

    val description: String? = null,

    @field:Min(1)
    @field:Max(12)
    val gradeLevel: Int? = null,

    @field:Size(max = 100)
    val subject: String? = null,

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
