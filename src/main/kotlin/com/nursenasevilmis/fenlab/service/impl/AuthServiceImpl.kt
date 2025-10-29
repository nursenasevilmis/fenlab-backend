package com.nursenasevilmis.fenlab.service.impl

import com.nursenasevilmis.fenlab.dto.request.UserLoginRequestDTO
import com.nursenasevilmis.fenlab.dto.request.UserRegisterRequestDTO
import com.nursenasevilmis.fenlab.dto.response.AuthResponseDTO
import com.nursenasevilmis.fenlab.dto.response.UserResponseDTO
import com.nursenasevilmis.fenlab.exception.BadRequestException
import com.nursenasevilmis.fenlab.exception.DuplicateResourceException
import com.nursenasevilmis.fenlab.exception.UnauthorizedException
import com.nursenasevilmis.fenlab.mapper.UserMapper
import com.nursenasevilmis.fenlab.model.User
import com.nursenasevilmis.fenlab.repository.UserRepository
import com.nursenasevilmis.fenlab.security.JwtTokenProvider
import com.nursenasevilmis.fenlab.service.AuthService
import com.nursenasevilmis.fenlab.util.ValidationUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

//experimentCount = 0 olarak dönüyor. Eğer kullanıcı deney sayısı da gösterilecekse, bu değeri DB’den çekmek gerekir.

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
    private val userMapper: UserMapper
) : AuthService {

    @Transactional
    override fun register(request: UserRegisterRequestDTO): AuthResponseDTO {
        // Validasyonlar
        if (userRepository.existsByUsername(request.username)) {
            throw DuplicateResourceException("Bu kullanıcı adı zaten kullanılıyor: ${request.username}")
        }

        if (userRepository.existsByEmail(request.email)) {
            throw DuplicateResourceException("Bu email adresi zaten kullanılıyor: ${request.email}")
        }

        if (!ValidationUtils.isValidEmail(request.email)) {
            throw BadRequestException("Geçersiz email formatı")
        }

        // Kullanıcı oluştur
        val user = User(
            username = request.username,
            fullName = request.fullName,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = request.role,
            branch = request.branch,
            experienceYears = request.experienceYears,
            bio = request.bio,
            profileImageUrl = request.profileImageUrl,
            createdAt = LocalDateTime.now()
        )

        val savedUser = userRepository.save(user)

        // Token oluştur
        val token = jwtTokenProvider.generateToken(
            userId = savedUser.id!!,
            username = savedUser.username,
            role = savedUser.role
        )

        return AuthResponseDTO(
            token = token,
            user = userMapper.toUserResponse(savedUser)
        )
    }

    @Transactional
    override fun login(request: UserLoginRequestDTO): AuthResponseDTO {
        // Authentication
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.usernameOrEmail,
                    request.password
                )
            )
        } catch (e: Exception) {
            throw UnauthorizedException("Kullanıcı adı veya şifre hatalı")
        }

        // Kullanıcıyı bul
        val user = userRepository.findByUsernameOrEmail(request.usernameOrEmail, request.usernameOrEmail)
            ?: throw UnauthorizedException("Kullanıcı bulunamadı")

        if (user.isDeleted) {
            throw UnauthorizedException("Bu hesap silinmiş")
        }

        // Last login güncelle
        user.lastLogin = LocalDateTime.now()
        userRepository.save(user)

        // Token oluştur
        val token = jwtTokenProvider.generateToken(
            userId = user.id!!,
            username = user.username,
            role = user.role
        )

        return AuthResponseDTO(
            token = token,
            user = userMapper.toUserResponse(user)
        )
    }
}