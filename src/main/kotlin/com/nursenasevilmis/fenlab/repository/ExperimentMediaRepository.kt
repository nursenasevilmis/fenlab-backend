package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.ExperimentMedia
import com.nursenasevilmis.fenlab.model.enums.MediaType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ExperimentMediaRepository : JpaRepository<ExperimentMedia, Long> {

    fun findByExperimentIdOrderByMediaOrderAsc(experimentId: Long): List<ExperimentMedia>

    fun findByExperimentIdAndMediaType(experimentId: Long, mediaType: MediaType): List<ExperimentMedia>

    @Query("""
        SELECT m FROM ExperimentMedia m 
        WHERE m.experiment.id = :experimentId 
        AND m.mediaType = :mediaType 
        ORDER BY m.mediaOrder ASC
    """)
    fun findFirstByExperimentIdAndMediaType(
        @Param("experimentId") experimentId: Long,
        @Param("mediaType") mediaType: MediaType
    ): List<ExperimentMedia>

    fun deleteByExperimentId(experimentId: Long)
}