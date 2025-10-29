package com.nursenasevilmis.fenlab.dto.response
//Tüm başarılı yanıtların ortak yapısı:Frontend, bu yapıyla her yanıtı tutarlı şekilde alır.
data class ApiResponseDTO<T>(
    val success: Boolean,
    val message: String?,
    val data: T?
)
