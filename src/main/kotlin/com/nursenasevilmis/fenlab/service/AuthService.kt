package com.nursenasevilmis.fenlab.service

import com.nursenasevilmis.fenlab.dto.request.UserLoginRequestDTO
import com.nursenasevilmis.fenlab.dto.request.UserRegisterRequestDTO
import com.nursenasevilmis.fenlab.dto.response.AuthResponseDTO


interface AuthService {
    fun register(request: UserRegisterRequestDTO): AuthResponseDTO
    fun login(request: UserLoginRequestDTO): AuthResponseDTO
}