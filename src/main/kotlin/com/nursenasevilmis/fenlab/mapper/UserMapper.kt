package com.nursenasevilmis.fenlab.mapper

import com.nursenasevilmis.fenlab.dto.response.UserResponseDTO
import com.nursenasevilmis.fenlab.dto.response.UserSummaryResponseDTO
import com.nursenasevilmis.fenlab.model.User
import org.springframework.stereotype.Component


@Component
class UserMapper {

    fun toUserResponse(user: User, experimentCount: Long = 0): UserResponseDTO {
        return UserResponseDTO(
            id = user.id!!,
            username = user.username,
            fullName = user.fullName,
            email = user.email,
            role = user.role,
            branch = user.branch,
            experienceYears = user.experienceYears,
            bio = user.bio,
            profileImageUrl = user.profileImageUrl,
            createdAt = user.createdAt,
            lastLogin = user.lastLogin,
            experimentCount = experimentCount
        )
    }

    fun toUserSummary(user: User): UserSummaryResponseDTO {
        return UserSummaryResponseDTO(
            id = user.id!!,
            username = user.username,
            fullName = user.fullName,
            role = user.role,
            profileImageUrl = user.profileImageUrl
        )
    }
}
