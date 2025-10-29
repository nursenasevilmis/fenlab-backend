package com.nursenasevilmis.fenlab.dto.response

import com.nursenasevilmis.fenlab.model.enums.UserRole
import java.time.LocalDateTime
//Kullanıcı profili veya “benim hesabım” sayfası için.
data class UserResponseDTO(
    val id: Long,
    val username: String,
    val fullName: String,
    val email: String,
    val role: UserRole,
    val branch: String?,
    val experienceYears: Int?,
    val bio: String?,
    val profileImageUrl: String?,
    val createdAt: LocalDateTime,
    val lastLogin: LocalDateTime?,
    val experimentCount: Long = 0
)
