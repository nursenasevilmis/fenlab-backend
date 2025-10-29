package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.Question
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QuestionRepository : JpaRepository<Question, Long> {
    @Query("""
        SELECT q FROM Question q 
        WHERE q.experiment.id = :experimentId 
        AND q.isDeleted = false
        ORDER BY q.createdAt DESC
    """)
    fun findByExperimentId(experimentId: Long, pageable: Pageable): Page<Question>

    @Query("""
        SELECT q FROM Question q 
        WHERE q.asker.id = :userId 
        AND q.isDeleted = false
        ORDER BY q.createdAt DESC
    """)
    fun findByAskerId(userId: Long, pageable: Pageable): Page<Question>

    @Query("""
        SELECT q FROM Question q 
        WHERE q.experiment.user.id = :userId 
        AND q.answerText IS NULL
        AND q.isDeleted = false
        ORDER BY q.createdAt DESC
    """)
    fun findUnansweredByExperimentOwnerId(userId: Long, pageable: Pageable): Page<Question>

    fun countByExperimentIdAndIsDeletedFalse(experimentId: Long): Long
}