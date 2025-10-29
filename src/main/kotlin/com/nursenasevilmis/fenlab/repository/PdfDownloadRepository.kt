package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.PdfDownload
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PdfDownloadRepository : JpaRepository<PdfDownload, Long> {

    fun findByUserId(userId: Long, pageable: Pageable): Page<PdfDownload>

    fun countByExperimentId(experimentId: Long): Long

    @Query("""
        SELECT COUNT(p) FROM PdfDownload p 
        WHERE p.experiment.id = :experimentId 
        AND p.downloadedAt >= :startDate
    """)
    fun countByExperimentIdAndDownloadedAtAfter(experimentId: Long, startDate: LocalDateTime): Long

    @Query("""
        SELECT p.experiment.id, COUNT(p) FROM PdfDownload p 
        WHERE p.downloadedAt >= :startDate
        GROUP BY p.experiment.id
        ORDER BY COUNT(p) DESC
    """)
    fun getMostDownloadedExperiments(startDate: LocalDateTime, pageable: Pageable): Page<Pair<Long, Long>>
    fun findByUserIdAndExperimentId(userId: Long, experimentId: Long): PdfDownload?

}