package com.nursenasevilmis.fenlab.model

import jakarta.persistence.*


@Entity
@Table(
    name = "experiment_steps",
    uniqueConstraints = [UniqueConstraint(columnNames = ["experiment_id", "step_order"])]
)
data class ExperimentStep(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    val experiment: Experiment,

    @Column(name = "step_order", nullable = false)
    val stepOrder: Int,

    @Column(name = "step_text", nullable = false, columnDefinition = "TEXT")
    val stepText: String
)