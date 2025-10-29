package com.nursenasevilmis.fenlab.service

import com.nursenasevilmis.fenlab.dto.request.UserUpdateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.UserResponseDTO
import com.nursenasevilmis.fenlab.dto.response.UserSummaryResponseDTO
import com.nursenasevilmis.fenlab.model.User

interface UserService {
    fun getUserById(userId: Long): UserResponseDTO
    fun getCurrentUser(): UserResponseDTO
    fun updateUser(userId: Long, request: UserUpdateRequestDTO): UserResponseDTO
    fun deleteUser(userId: Long)
    fun mapToUserSummary(user: User): UserSummaryResponseDTO
}