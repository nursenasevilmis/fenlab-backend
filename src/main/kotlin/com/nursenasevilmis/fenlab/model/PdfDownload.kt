package com.nursenasevilmis.fenlab.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "pdf_downloads")
data class PdfDownload(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    val experiment: Experiment,

    @Column(name = "downloaded_at")
    var downloadedAt: LocalDateTime = LocalDateTime.now()
)