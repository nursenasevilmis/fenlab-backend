package com.nursenasevilmis.fenlab.model

import com.nursenasevilmis.fenlab.model.enums.MediaType
import jakarta.persistence.*

@Entity
@Table(name = "experiment_media")
data class ExperimentMedia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    val experiment: Experiment,

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    val mediaType: MediaType,

    @Column(name = "media_url", nullable = false, columnDefinition = "TEXT")
    val mediaUrl: String,

    @Column(name = "media_order")
    val mediaOrder: Int = 0
)
