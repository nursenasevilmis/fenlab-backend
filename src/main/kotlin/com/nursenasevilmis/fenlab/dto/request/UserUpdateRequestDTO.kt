package com.nursenasevilmis.fenlab.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
//Kullanıcı kendi profilini güncellemek istediğinde kullanılır:
data class UserUpdateRequestDTO(
    @field:Size(max = 100)
    val fullName: String? = null,

    @field:Email
    @field:Size(max = 100)
    val email: String? = null,

    @field:Size(max = 100)
    val branch: String? = null,

    @field:Min(0)
    val experienceYears: Int? = null,

    val bio: String? = null,
    val profileImageUrl: String? = null,

)
