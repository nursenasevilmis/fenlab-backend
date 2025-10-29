package com.nursenasevilmis.fenlab.dto.request

import com.nursenasevilmis.fenlab.model.enums.UserRole
import jakarta.validation.constraints.*
//Kullanıcı kayıt formundaki tüm alanlar buradan gelir.

data class UserRegisterRequestDTO (
    @field:NotBlank(message = "Kullanıcı adı boş olamaz")
    @field:Size(min = 3, max = 50, message = "Kullanıcı adı 3-50 karakter arasında olmalıdır")
    val username: String,

    @field:NotBlank(message = "Ad soyad boş olamaz")
    @field:Size(max = 100, message = "Ad soyad maksimum 100 karakter olabilir")
    val fullName: String,

    @field:NotBlank(message = "Email boş olamaz")
    @field:Email(message = "Geçerli bir email adresi giriniz")
    @field:Size(max = 100)
    val email: String,

    @field:NotBlank(message = "Şifre boş olamaz")
    @field:Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    val password: String,

    @field:NotNull(message = "Rol seçilmelidir")
    val role: UserRole,

    @field:Size(max = 100)
    val branch: String? = null,

    @field:Min(value = 0, message = "Deneyim yılı negatif olamaz")
    val experienceYears: Int? = null,

    val bio: String? = null,
    val profileImageUrl: String? = null
)