package com.nursenasevilmis.fenlab.service.impl


import com.nursenasevilmis.fenlab.dto.request.ExperimentCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.request.ExperimentFilterRequestDTO
import com.nursenasevilmis.fenlab.dto.request.ExperimentUpdateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.ExperimentResponseDTO
import com.nursenasevilmis.fenlab.dto.response.ExperimentSummaryResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.dto.response.MediaResponseDTO
import com.nursenasevilmis.fenlab.dto.response.MaterialResponseDTO
import com.nursenasevilmis.fenlab.dto.response.StepResponseDTO
import com.nursenasevilmis.fenlab.exception.ForbiddenException
import com.nursenasevilmis.fenlab.exception.ResourceNotFoundException
import com.nursenasevilmis.fenlab.mapper.ExperimentMapper
import com.nursenasevilmis.fenlab.model.Experiment
import com.nursenasevilmis.fenlab.model.ExperimentMaterial
import com.nursenasevilmis.fenlab.model.ExperimentStep
import com.nursenasevilmis.fenlab.model.ExperimentMedia
import com.nursenasevilmis.fenlab.model.enums.MediaType
import com.nursenasevilmis.fenlab.model.enums.SortType
import com.nursenasevilmis.fenlab.repository.*
import com.nursenasevilmis.fenlab.service.ExperimentService
import com.nursenasevilmis.fenlab.service.UserService
import com.nursenasevilmis.fenlab.util.PaginationUtils
import com.nursenasevilmis.fenlab.util.SecurityUtils
import jakarta.transaction.Transactional
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class ExperimentServiceImpl(
    private val experimentRepository: ExperimentRepository,
    private val experimentMaterialRepository: ExperimentMaterialRepository,
    private val experimentStepRepository: ExperimentStepRepository,
    private val experimentMediaRepository: ExperimentMediaRepository,
    private val userRepository: UserRepository,
    private val experimentMapper: ExperimentMapper
) : ExperimentService {

    override fun getAllExperiments(filterRequest: ExperimentFilterRequestDTO): PaginatedResponseDTO<ExperimentSummaryResponseDTO> {
        val sort = when (filterRequest.sortType) {
            SortType.MOST_RECENT -> Sort.by(Sort.Direction.DESC, "createdAt")
            SortType.OLDEST -> Sort.by(Sort.Direction.ASC, "createdAt")
            SortType.HIGHEST_RATED -> Sort.by(Sort.Direction.DESC, "createdAt")
            SortType.MOST_FAVORITED -> Sort.by(Sort.Direction.DESC, "createdAt")
        }

        val pageable = PaginationUtils.createPageable(filterRequest.page, filterRequest.size, sort)

        val page = experimentRepository.findByFilters(
            subject = filterRequest.subject,
            gradeLevel = filterRequest.gradeLevel,
            difficulty = filterRequest.difficulty,
            search = filterRequest.search,
            pageable = pageable
        )

        val currentUserId = try {
            SecurityUtils.getCurrentUserId()
        } catch (e: Exception) {
            null
        }

        val summaries = page.content.map { experiment ->
            experimentMapper.toExperimentSummary(experiment, currentUserId)
        }

        return PaginatedResponseDTO(
            content = summaries,
            page = page.number,
            size = page.size,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            isFirst = page.isFirst,
            isLast = page.isLast
        )
    }

    override fun getExperimentById(experimentId: Long): ExperimentResponseDTO {
        val experiment = experimentRepository.findById(experimentId)
            .orElseThrow { ResourceNotFoundException("Deney bulunamadı: $experimentId") }

        if (!experiment.isPublished) {
            val currentUserId = try {
                SecurityUtils.getCurrentUserId()
            } catch (e: Exception) {
                throw ForbiddenException("Bu deneye erişim izniniz yok")
            }

            if (experiment.user.id != currentUserId) {
                throw ForbiddenException("Bu deneye erişim izniniz yok")
            }
        }

        val currentUserId = try {
            SecurityUtils.getCurrentUserId()
        } catch (e: Exception) {
            null
        }

        return experimentMapper.toExperimentResponse(experiment, currentUserId)
    }

    @Transactional
    override fun createExperiment(request: ExperimentCreateRequestDTO): ExperimentResponseDTO {
        // TEST MODE: Geçici olarak user id 1 kullan
        val currentUserId = 2L // SecurityUtils.getCurrentUserId()

        val user = userRepository.findById(currentUserId)
            .orElseThrow { ResourceNotFoundException("Kullanıcı bulunamadı") }

       // if (!SecurityUtils.isTeacher()) {
          //  throw ForbiddenException("Sadece öğretmenler deney oluşturabilir")
      //  }

        // Experiment oluştur
        val experiment = Experiment(
            user = user,
            title = request.title,
            description = request.description,
            gradeLevel = request.gradeLevel,
            subject = request.subject,
            difficulty = request.difficulty,
            expectedResult = request.expectedResult,
            safetyNotes = request.safetyNotes,
            isPublished = request.isPublished,
            createdAt = LocalDateTime.now()
        )

        val savedExperiment = experimentRepository.save(experiment)

        // Materials ekle
        request.materials.forEach { materialReq ->
            val material = ExperimentMaterial(
                experiment = savedExperiment,
                materialName = materialReq.materialName,
                quantity = materialReq.quantity
            )
            experimentMaterialRepository.save(material)
        }

        // Steps ekle
        request.steps.forEach { stepReq ->
            val step = ExperimentStep(
                experiment = savedExperiment,
                stepOrder = stepReq.stepOrder,
                stepText = stepReq.stepText
            )
            experimentStepRepository.save(step)
        }

        // Media ekle
        request.media.forEach { mediaReq ->
            val media = ExperimentMedia(
                experiment = savedExperiment,
                mediaType = mediaReq.mediaType,
                mediaUrl = mediaReq.mediaUrl,
                mediaOrder = mediaReq.mediaOrder
            )
            experimentMediaRepository.save(media)
        }

        return getExperimentById(savedExperiment.id!!)
    }

    @Transactional
    override fun updateExperiment(experimentId: Long, request: ExperimentUpdateRequestDTO): ExperimentResponseDTO {
        val experiment = experimentRepository.findById(experimentId)
            .orElseThrow { ResourceNotFoundException("Deney bulunamadı: $experimentId") }

       // val currentUserId = SecurityUtils.getCurrentUserId()
        val currentUserId = 2L
       // if (experiment.user.id != currentUserId) {
            //throw ForbiddenException("Sadece kendi deneyinizi güncelleyebilirsiniz")
        //}

        // Experiment güncelle
        val updatedExperiment = experiment.copy(
            title = request.title ?: experiment.title,
            description = request.description ?: experiment.description,
            gradeLevel = request.gradeLevel ?: experiment.gradeLevel,
            subject = request.subject ?: experiment.subject,
            difficulty = request.difficulty ?: experiment.difficulty,
            expectedResult = request.expectedResult ?: experiment.expectedResult,
            safetyNotes = request.safetyNotes ?: experiment.safetyNotes,
            isPublished = request.isPublished ?: experiment.isPublished,
            updatedAt = LocalDateTime.now()
        )

        experimentRepository.save(updatedExperiment)

        // Materials güncelle
        request.materials?.let { materials ->
            experimentMaterialRepository.deleteByExperimentId(experimentId)
            materials.forEach { materialReq ->
                val material = ExperimentMaterial(
                    experiment = updatedExperiment,
                    materialName = materialReq.materialName,
                    quantity = materialReq.quantity
                )
                experimentMaterialRepository.save(material)
            }
        }

        // Steps güncelle
        request.steps?.let { steps ->
            experimentStepRepository.deleteByExperimentId(experimentId)
            steps.forEach { stepReq ->
                val step = ExperimentStep(
                    experiment = updatedExperiment,
                    stepOrder = stepReq.stepOrder,
                    stepText = stepReq.stepText
                )
                experimentStepRepository.save(step)
            }
        }

        // Media güncelle
        request.media?.let { mediaList ->
            experimentMediaRepository.deleteByExperimentId(experimentId)
            mediaList.forEach { mediaReq ->
                val media = ExperimentMedia(
                    experiment = updatedExperiment,
                    mediaType = mediaReq.mediaType,
                    mediaUrl = mediaReq.mediaUrl,
                    mediaOrder = mediaReq.mediaOrder
                )
                experimentMediaRepository.save(media)
            }
        }

        return getExperimentById(experimentId)
    }

    @Transactional
    override fun deleteExperiment(experimentId: Long) {
        val experiment = experimentRepository.findById(experimentId)
            .orElseThrow { ResourceNotFoundException("Deney bulunamadı: $experimentId") }

        //val currentUserId = SecurityUtils.getCurrentUserId()
        val currentUserId = 2L
        //if (experiment.user.id != currentUserId) {
          //  throw ForbiddenException("Sadece kendi deneyinizi silebilirsiniz")
        //}

        // Soft delete
        val deletedExperiment = experiment.copy(isDeleted = true)
        experimentRepository.save(deletedExperiment)
    }

    override fun getUserExperiments(userId: Long, page: Int, size: Int): PaginatedResponseDTO<ExperimentSummaryResponseDTO> {
        val pageable = PaginationUtils.createPageable(page, size)
        val experimentsPage = experimentRepository.findByUserId(userId, pageable)

        val currentUserId = try {
            SecurityUtils.getCurrentUserId()
        } catch (e: Exception) {
            null
        }

        val summaries = experimentsPage.content.map { experiment ->
            experimentMapper.toExperimentSummary(experiment, currentUserId)
        }

        return PaginatedResponseDTO(
            content = summaries,
            page = experimentsPage.number,
            size = experimentsPage.size,
            totalElements = experimentsPage.totalElements,
            totalPages = experimentsPage.totalPages,
            isFirst = experimentsPage.isFirst,
            isLast = experimentsPage.isLast
        )
    }

    override fun getAllSubjects(): List<String> {
        return experimentRepository.findAllSubjects()
    }

    override fun mapToExperimentSummary(experiment: Experiment, currentUserId: Long?): ExperimentSummaryResponseDTO {
        return experimentMapper.toExperimentSummary(experiment, currentUserId)
    }
}