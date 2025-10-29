package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.ExperimentMaterial
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExperimentMaterialRepository : JpaRepository<ExperimentMaterial, Long> {

    fun findByExperimentId(experimentId: Long): List<ExperimentMaterial>

    fun deleteByExperimentId(experimentId: Long)
}