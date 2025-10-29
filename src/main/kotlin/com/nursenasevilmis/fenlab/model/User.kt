package com.nursenasevilmis.fenlab.model

import com.nursenasevilmis.fenlab.model.enums.UserRole
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false, length = 50)
    val username: String,

    @Column(name = "full_name", nullable = false, length = 100)
    val fullName: String,

    @Column(unique = true, nullable = false, length = 100)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole,

    @Column(length = 100)
    val branch: String? = null,

    @Column(name = "experience_years")
    val experienceYears: Int? = null,

    @Column(columnDefinition = "TEXT")
    val bio: String? = null,

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    val profileImageUrl: String? = null,

    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "last_login")
    var lastLogin: LocalDateTime? = null
)