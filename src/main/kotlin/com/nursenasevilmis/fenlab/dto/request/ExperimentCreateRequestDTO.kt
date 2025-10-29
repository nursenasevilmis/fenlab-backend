package com.nursenasevilmis.fenlab.dto.request

import com.nursenasevilmis.fenlab.model.enums.DifficultyLevel
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotNull
//Yeni bir deney oluşturma formunun tamamını taşır.
data class ExperimentCreateRequestDTO(
    @field:NotBlank(message = "Başlık boş olamaz")
    @field:Size(max = 255)
    val title: String,

    @field:NotBlank(message = "Açıklama boş olamaz")
    val description: String,

    @field:NotNull(message = "Sınıf seviyesi belirtilmelidir")
    @field:Min(value = 1, message = "Sınıf seviyesi en az 1 olmalıdır")
    @field:Max(value = 12, message = "Sınıf seviyesi en fazla 12 olabilir")
    val gradeLevel: Int,

    @field:NotBlank(message = "Ders alanı boş olamaz")
    @field:Size(max = 100)
    val subject: String,

    @field:NotNull(message = "Zorluk seviyesi belirtilmelidir")
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
