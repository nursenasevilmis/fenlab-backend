package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {

    @Query("""
        SELECT c FROM Comment c 
        WHERE c.experiment.id = :experimentId 
        AND c.isDeleted = false
        ORDER BY c.createdAt DESC
    """)
    fun findByExperimentId(experimentId: Long, pageable: Pageable): Page<Comment>

    fun countByExperimentIdAndIsDeletedFalse(experimentId: Long): Long

    @Query("""
        SELECT c FROM Comment c 
        WHERE c.user.id = :userId 
        AND c.isDeleted = false
        ORDER BY c.createdAt DESC
    """)
    fun findByUserId(userId: Long, pageable: Pageable): Page<Comment>
}