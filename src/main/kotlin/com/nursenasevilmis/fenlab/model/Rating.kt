package com.nursenasevilmis.fenlab.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "ratings",
    uniqueConstraints = [UniqueConstraint(columnNames = ["experiment_id", "user_id"])]
)
data class Rating(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    val experiment: Experiment,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val rating: Int,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
