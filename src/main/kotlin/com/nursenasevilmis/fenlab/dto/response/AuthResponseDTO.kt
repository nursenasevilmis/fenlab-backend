package com.nursenasevilmis.fenlab.dto.response
//Login işlemi sonrası dönen yanıt:Frontend login sonrası bu yanıtı alır.
data class AuthResponseDTO(
    val token: String,
    val user: UserResponseDTO
)
