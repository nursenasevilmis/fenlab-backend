package com.nursenasevilmis.fenlab.service

import com.nursenasevilmis.fenlab.dto.request.ExperimentCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.request.ExperimentFilterRequestDTO
import com.nursenasevilmis.fenlab.dto.request.ExperimentUpdateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.ExperimentResponseDTO
import com.nursenasevilmis.fenlab.dto.response.ExperimentSummaryResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.model.Experiment

interface ExperimentService {
    fun getAllExperiments(filterRequest: ExperimentFilterRequestDTO): PaginatedResponseDTO<ExperimentSummaryResponseDTO>
    fun getExperimentById(experimentId: Long): ExperimentResponseDTO
    fun createExperiment(request: ExperimentCreateRequestDTO): ExperimentResponseDTO
    fun updateExperiment(experimentId: Long, request: ExperimentUpdateRequestDTO): ExperimentResponseDTO
    fun deleteExperiment(experimentId: Long)
    fun getUserExperiments(userId: Long, page: Int, size: Int): PaginatedResponseDTO<ExperimentSummaryResponseDTO>
    fun getAllSubjects(): List<String>
    fun mapToExperimentSummary(experiment: Experiment, currentUserId: Long?): ExperimentSummaryResponseDTO
}