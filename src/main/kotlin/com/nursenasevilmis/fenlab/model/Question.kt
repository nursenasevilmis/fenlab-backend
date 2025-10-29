package com.nursenasevilmis.fenlab.model


import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "questions")
data class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", nullable = false)
    val experiment: Experiment,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asker_id", nullable = false)
    val asker: User,

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    val questionText: String,

    @Column(name = "answer_text", columnDefinition = "TEXT")
    var answerText: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answerer_id")
    var answerer: User? = null,

    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "answered_at")
    var answeredAt: LocalDateTime? = null
)
