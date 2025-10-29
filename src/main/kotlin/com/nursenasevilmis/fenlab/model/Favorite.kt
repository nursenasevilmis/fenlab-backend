package com.nursenasevilmis.fenlab.model

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(
    name = "favorites",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "experiment_id"])]
)
data class Favorite(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    val experiment: Experiment,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)