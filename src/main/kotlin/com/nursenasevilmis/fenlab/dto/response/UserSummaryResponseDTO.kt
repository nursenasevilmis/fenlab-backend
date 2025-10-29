package com.nursenasevilmis.fenlab.dto.response

import com.nursenasevilmis.fenlab.model.enums.UserRole
//Deney sayfasında “yayınlayan kullanıcı” bilgisi için.
data class UserSummaryResponseDTO(
    val id: Long,
    val username: String,
    val fullName: String,
    val role: UserRole,
    val profileImageUrl: String?
)
