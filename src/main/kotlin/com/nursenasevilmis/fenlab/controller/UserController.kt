package com.nursenasevilmis.fenlab.controller

import com.nursenasevilmis.fenlab.dto.request.UserUpdateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.UserResponseDTO
import com.nursenasevilmis.fenlab.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Kullanıcı işlemleri")
@SecurityRequirement(name = "bearerAuth") // Swagger JWT güvenliği için
class UserController(
    private val userService: UserService
) {

    // ==============================
    // 1️⃣ Mevcut kullanıcı bilgilerini getir
    // ==============================
    @GetMapping("/me")
    @Operation(summary = "Giriş yapmış kullanıcının bilgilerini getirir")
    fun getCurrentUser(): ResponseEntity<UserResponseDTO> {
        val response = userService.getCurrentUser()
        return ResponseEntity.ok(response)
    }

    // ==============================
    // 2️⃣ Belirli bir kullanıcıyı getir
    // ==============================
    @GetMapping("/{userId}")
    @Operation(summary = "Kullanıcı ID'sine göre kullanıcı bilgilerini getirir")
    fun getUserById(
        @PathVariable userId: Long
    ): ResponseEntity<UserResponseDTO> {
        val response = userService.getUserById(userId)
        return ResponseEntity.ok(response)
    }

    // ==============================
    // 3️⃣ Kullanıcı bilgilerini güncelle
    // ==============================
    @PutMapping("/{userId}")
    @Operation(summary = "Giriş yapmış kullanıcı kendi profil bilgilerini günceller")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody request: UserUpdateRequestDTO
    ): ResponseEntity<UserResponseDTO> {
        val response = userService.updateUser(userId, request)
        return ResponseEntity.ok(response)
    }

    // ==============================
    // 4️⃣ Kullanıcıyı sil (soft delete)
    // ==============================
    @DeleteMapping("/{userId}")
    @Operation(summary = "Kullanıcı kendi hesabını siler (soft delete)")
    fun deleteUser(
        @PathVariable userId: Long
    ): ResponseEntity<String> {
        userService.deleteUser(userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
