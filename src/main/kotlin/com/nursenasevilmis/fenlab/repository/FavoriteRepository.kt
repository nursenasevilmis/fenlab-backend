package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.Favorite
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FavoriteRepository : JpaRepository<Favorite, Long> {

    fun findByUserIdAndExperimentId(userId: Long, experimentId: Long): Favorite?

    fun existsByUserIdAndExperimentId(userId: Long, experimentId: Long): Boolean

    fun deleteByUserIdAndExperimentId(userId: Long, experimentId: Long)

    @Query("""
        SELECT f FROM Favorite f 
        JOIN FETCH f.experiment e
        WHERE f.user.id = :userId 
        AND e.isDeleted = false
    """)
    fun findByUserId(userId: Long, pageable: Pageable): Page<Favorite>

    fun countByExperimentId(experimentId: Long): Long

    @Query("""
        SELECT COUNT(f) FROM Favorite f 
        WHERE f.experiment.id IN :experimentIds
        GROUP BY f.experiment.id
    """)
    fun countFavoritesByExperimentIds(experimentIds: List<Long>): Map<Long, Long>
}
