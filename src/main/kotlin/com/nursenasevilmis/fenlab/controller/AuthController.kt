package com.nursenasevilmis.fenlab.controller

import com.nursenasevilmis.fenlab.dto.request.UserLoginRequestDTO
import com.nursenasevilmis.fenlab.dto.request.UserRegisterRequestDTO
import com.nursenasevilmis.fenlab.dto.response.AuthResponseDTO
import com.nursenasevilmis.fenlab.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Kullanıcı kayıt ve giriş işlemleri")
class AuthController(
    private val authService: AuthService
) {

    // ==============================
    // 1️⃣ Kullanıcı kayıt işlemi
    // ==============================
    @PostMapping("/register")
    @Operation(summary = "Yeni kullanıcı kaydı oluşturur")
    fun register(
        @RequestBody request: UserRegisterRequestDTO
    ): ResponseEntity<AuthResponseDTO> {
        val response = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ==============================
    // 2️⃣ Kullanıcı giriş işlemi
    // ==============================
    @PostMapping("/login")
    @Operation(summary = "Kullanıcı girişi yapar ve JWT token döner")
    fun login(
        @RequestBody request: UserLoginRequestDTO
    ): ResponseEntity<AuthResponseDTO> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }
}
