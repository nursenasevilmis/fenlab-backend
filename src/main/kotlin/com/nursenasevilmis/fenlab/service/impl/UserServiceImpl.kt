package com.nursenasevilmis.fenlab.service.impl

import com.nursenasevilmis.fenlab.dto.request.UserUpdateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.UserResponseDTO
import com.nursenasevilmis.fenlab.dto.response.UserSummaryResponseDTO
import com.nursenasevilmis.fenlab.exception.ForbiddenException
import com.nursenasevilmis.fenlab.exception.ResourceNotFoundException
import com.nursenasevilmis.fenlab.mapper.UserMapper
import com.nursenasevilmis.fenlab.model.User
import com.nursenasevilmis.fenlab.repository.ExperimentRepository
import com.nursenasevilmis.fenlab.repository.UserRepository
import com.nursenasevilmis.fenlab.service.UserService
import com.nursenasevilmis.fenlab.util.SecurityUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val experimentRepository: ExperimentRepository,
    private val userMapper: UserMapper
) : UserService {

    override fun getUserById(userId: Long): UserResponseDTO {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("Kullanıcı bulunamadı: $userId") }
        val experimentCount = experimentRepository.countByUserId(userId)
        return userMapper.toUserResponse(user, experimentCount)
    }

    override fun getCurrentUser(): UserResponseDTO {
        val currentUserId = SecurityUtils.getCurrentUserId()
        return getUserById(currentUserId)
    }

    @Transactional
    override fun updateUser(userId: Long, request: UserUpdateRequestDTO): UserResponseDTO {
        val currentUserId = SecurityUtils.getCurrentUserId()
        if (currentUserId != userId) throw ForbiddenException("Sadece kendi profilinizi güncelleyebilirsiniz")

        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("Kullanıcı bulunamadı: $userId") }

        val updatedUser = user.copy(
            fullName        = request.fullName ?: user.fullName,
            email           = request.email ?: user.email,
            branch          = request.branch ?: user.branch,
            experienceYears = request.experienceYears ?: user.experienceYears,
            bio             = request.bio ?: user.bio,
            profileImageUrl = request.profileImageUrl ?: user.profileImageUrl
        )

        val savedUser = userRepository.save(updatedUser)
        val experimentCount = experimentRepository.countByUserId(userId)
        return userMapper.toUserResponse(savedUser, experimentCount)
    }

    @Transactional
    override fun deleteUser(userId: Long) {
        val currentUserId = SecurityUtils.getCurrentUserId()
        if (currentUserId != userId) throw ForbiddenException("Sadece kendi hesabınızı silebilirsiniz")

        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("Kullanıcı bulunamadı: $userId") }

        userRepository.save(user.copy(isDeleted = true))
    }

    override fun mapToUserSummary(user: User): UserSummaryResponseDTO {
        return userMapper.toUserSummary(user)
    }

    override fun searchUsers(query: String): List<UserResponseDTO> {
        val users = userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(query, query)
        return users.map { user ->
            val uid = user.id ?: return@map userMapper.toUserResponse(user, 0L)
            val experimentCount = experimentRepository.countByUserId(uid)
            userMapper.toUserResponse(user, experimentCount)
        }
    }
}