package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.ExperimentStep
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExperimentStepRepository : JpaRepository<ExperimentStep, Long> {

    fun findByExperimentIdOrderByStepOrderAsc(experimentId: Long): List<ExperimentStep>

    fun deleteByExperimentId(experimentId: Long)
}