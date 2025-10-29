package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.Experiment
import com.nursenasevilmis.fenlab.model.enums.DifficultyLevel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExperimentRepository : JpaRepository<Experiment, Long> {

    @Query("""
        SELECT e FROM Experiment e 
        WHERE e.isPublished = true 
        AND e.isDeleted = false
    """)
    fun findAllPublished(pageable: Pageable): Page<Experiment>

    @Query("""
        SELECT e FROM Experiment e 
        WHERE e.user.id = :userId 
        AND e.isDeleted = false
    """)
    fun findByUserId(@Param("userId") userId: Long, pageable: Pageable): Page<Experiment>

    @Query("""
        SELECT e FROM Experiment e 
        WHERE e.isPublished = true 
        AND e.isDeleted = false
        AND (:subject IS NULL OR e.subject = :subject)
        AND (:gradeLevel IS NULL OR e.gradeLevel = :gradeLevel)
        AND (:difficulty IS NULL OR e.difficulty = :difficulty)
        AND (:search IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%')) 
             OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    fun findByFilters(
        @Param("subject") subject: String?,
        @Param("gradeLevel") gradeLevel: Int?,
        @Param("difficulty") difficulty: DifficultyLevel?,
        @Param("search") search: String?,
        pageable: Pageable
    ): Page<Experiment>

    fun countByUserId(userId: Long): Long

    @Query("""
        SELECT DISTINCT e.subject FROM Experiment e 
        WHERE e.isPublished = true 
        AND e.isDeleted = false
        ORDER BY e.subject
    """)
    fun findAllSubjects(): List<String>
}