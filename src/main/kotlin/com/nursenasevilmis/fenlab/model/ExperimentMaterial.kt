package com.nursenasevilmis.fenlab.model

import jakarta.persistence.*


@Entity
@Table(name = "experiment_materials")
data class ExperimentMaterial(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    val experiment: Experiment,

    @Column(name = "material_name", nullable = false, length = 255)
    val materialName: String,

    @Column(nullable = false, length = 50)
    val quantity: String
)