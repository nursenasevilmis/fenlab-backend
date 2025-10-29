package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.Rating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RatingRepository : JpaRepository<Rating, Long> {
    fun findByUserIdAndExperimentId(userId: Long, experimentId: Long): Rating?

    fun existsByUserIdAndExperimentId(userId: Long, experimentId: Long): Boolean

    @Query("""
        SELECT AVG(r.rating) FROM Rating r 
        WHERE r.experiment.id = :experimentId
    """)
    fun getAverageRatingByExperimentId(experimentId: Long): Double?

    @Query("""
        SELECT r.experiment.id, AVG(r.rating) FROM Rating r 
        WHERE r.experiment.id IN :experimentIds
        GROUP BY r.experiment.id
    """)
    fun getAverageRatingsByExperimentIds(experimentIds: List<Long>): Map<Long, Double>
}
