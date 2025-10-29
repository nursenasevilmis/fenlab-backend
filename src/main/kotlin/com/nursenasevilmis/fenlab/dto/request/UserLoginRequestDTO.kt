package com.nursenasevilmis.fenlab.dto.request

import jakarta.validation.constraints.NotBlank
//Login endpoint’inde (örneğin /auth/login) backend bu DTO’yu bekler.
data class UserLoginRequestDTO(
    @field:NotBlank(message = "Kullanıcı adı veya email boş olamaz")
    val usernameOrEmail: String,

    @field:NotBlank(message = "Şifre boş olamaz")
    val password: String
)
