package com.nursenasevilmis.fenlab.model

import com.nursenasevilmis.fenlab.model.enums.DifficultyLevel
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "experiments")
data class Experiment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false, length = 255)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(name = "grade_level", nullable = false)
    val gradeLevel: Int,

    @Column(nullable = false, length = 100)
    val subject: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val difficulty: DifficultyLevel,

    @Column(name = "expected_result", columnDefinition = "TEXT")
    val expectedResult: String? = null,

    @Column(name = "safety_notes", columnDefinition = "TEXT")
    val safetyNotes: String? = null,

    @Column(name = "is_published")
    val isPublished: Boolean = true,

    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "experiment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val materials: MutableList<ExperimentMaterial> = mutableListOf(),

    @OneToMany(mappedBy = "experiment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val steps: MutableList<ExperimentStep> = mutableListOf(),

    @OneToMany(mappedBy = "experiment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val media: MutableList<ExperimentMedia> = mutableListOf()
)
