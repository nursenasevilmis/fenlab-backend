package com.nursenasevilmis.fenlab.mapper

import com.nursenasevilmis.fenlab.dto.response.*
import com.nursenasevilmis.fenlab.model.Experiment
import com.nursenasevilmis.fenlab.model.ExperimentMaterial
import com.nursenasevilmis.fenlab.model.ExperimentMedia
import com.nursenasevilmis.fenlab.model.ExperimentStep
import com.nursenasevilmis.fenlab.model.enums.MediaType
import com.nursenasevilmis.fenlab.repository.*
import org.springframework.stereotype.Component


@Component
class ExperimentMapper(
    private val userMapper: UserMapper,
    private val experimentMaterialRepository: ExperimentMaterialRepository,
    private val experimentStepRepository: ExperimentStepRepository,
    private val experimentMediaRepository: ExperimentMediaRepository,
    private val favoriteRepository: FavoriteRepository,
    private val ratingRepository: RatingRepository,
    private val commentRepository: CommentRepository,
    private val questionRepository: QuestionRepository
) {

    fun toExperimentResponse(experiment: Experiment, currentUserId: Long?): ExperimentResponseDTO {
        val materials = experimentMaterialRepository.findByExperimentId(experiment.id!!)
        val steps = experimentStepRepository.findByExperimentIdOrderByStepOrderAsc(experiment.id!!)
        val media = experimentMediaRepository.findByExperimentIdOrderByMediaOrderAsc(experiment.id!!)

        val favoriteCount = favoriteRepository.countByExperimentId(experiment.id!!)
        val averageRating = ratingRepository.getAverageRatingByExperimentId(experiment.id!!)
        val commentCount = commentRepository.countByExperimentIdAndIsDeletedFalse(experiment.id!!)
        val questionCount = questionRepository.countByExperimentIdAndIsDeletedFalse(experiment.id!!)

        val isFavorited = currentUserId?.let {
            favoriteRepository.existsByUserIdAndExperimentId(it, experiment.id!!)
        } ?: false

        val userRating = currentUserId?.let {
            ratingRepository.findByUserIdAndExperimentId(it, experiment.id!!)?.rating
        }

        return ExperimentResponseDTO(
            id = experiment.id!!,
            user = userMapper.toUserSummary(experiment.user),
            title = experiment.title,
            description = experiment.description,
            gradeLevel = experiment.gradeLevel,
            subject = experiment.subject,
            difficulty = experiment.difficulty,
            expectedResult = experiment.expectedResult,
            safetyNotes = experiment.safetyNotes,
            isPublished = experiment.isPublished,
            createdAt = experiment.createdAt,
            updatedAt = experiment.updatedAt,
            materials = materials.map { toMaterialResponse(it) },
            steps = steps.map { toStepResponse(it) },
            media = media.map { toMediaResponse(it) },
            favoriteCount = favoriteCount,
            averageRating = averageRating,
            commentCount = commentCount,
            questionCount = questionCount,
            isFavoritedByCurrentUser = isFavorited,
            currentUserRating = userRating
        )
    }

    fun toExperimentSummary(experiment: Experiment, currentUserId: Long?): ExperimentSummaryResponseDTO {
        val favoriteCount = favoriteRepository.countByExperimentId(experiment.id!!)
        val averageRating = ratingRepository.getAverageRatingByExperimentId(experiment.id!!)
        val commentCount = commentRepository.countByExperimentIdAndIsDeletedFalse(experiment.id!!)

        val isFavorited = currentUserId?.let {
            favoriteRepository.existsByUserIdAndExperimentId(it, experiment.id!!)
        } ?: false

        // İlk video/image'i thumbnail olarak kullan
        val media = experimentMediaRepository.findByExperimentIdOrderByMediaOrderAsc(experiment.id!!)
        val firstVideo = media.firstOrNull { it.mediaType == MediaType.VIDEO }
        val firstImage = media.firstOrNull { it.mediaType == MediaType.IMAGE }

        return ExperimentSummaryResponseDTO(
            id = experiment.id!!,
            user = userMapper.toUserSummary(experiment.user),
            title = experiment.title,
            description = experiment.description,
            gradeLevel = experiment.gradeLevel,
            subject = experiment.subject,
            difficulty = experiment.difficulty,
            createdAt = experiment.createdAt,
            thumbnailUrl = firstImage?.mediaUrl,
            videoUrl = firstVideo?.mediaUrl,
            favoriteCount = favoriteCount,
            averageRating = averageRating,
            commentCount = commentCount,
            isFavoritedByCurrentUser = isFavorited
        )
    }

    fun toMaterialResponse(material: ExperimentMaterial): MaterialResponseDTO {
        return MaterialResponseDTO(
            id = material.id!!,
            materialName = material.materialName,
            quantity = material.quantity
        )
    }

    fun toStepResponse(step: ExperimentStep): StepResponseDTO {
        return StepResponseDTO(
            id = step.id!!,
            stepOrder = step.stepOrder,
            stepText = step.stepText
        )
    }

    fun toMediaResponse(media: ExperimentMedia): MediaResponseDTO {
        return MediaResponseDTO(
            id = media.id!!,
            mediaType = media.mediaType,
            mediaUrl = media.mediaUrl,
            mediaOrder = media.mediaOrder
        )
    }
}