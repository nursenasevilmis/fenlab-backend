package com.nursenasevilmis.fenlab.controller


import com.nursenasevilmis.fenlab.dto.response.NotificationResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.service.NotificationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Controller", description = "Bildirim işlemlerini yönetir.")
class NotificationController(
    private val notificationService: NotificationService
) {

    // ✅ Tüm bildirimleri getir
    @Operation(summary = "Kullanıcının tüm bildirimlerini getir")
    @GetMapping
    fun getUserNotifications(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponseDTO<NotificationResponseDTO>> {
        val notifications = notificationService.getUserNotifications(page, size)
        return ResponseEntity.ok(notifications)
    }

    // ✅ Okunmamış bildirimleri getir
    @Operation(summary = "Kullanıcının okunmamış bildirimlerini getir")
    @GetMapping("/unread")
    fun getUnreadNotifications(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponseDTO<NotificationResponseDTO>> {
        val unreadNotifications = notificationService.getUnreadNotifications(page, size)
        return ResponseEntity.ok(unreadNotifications)
    }

    // ✅ Tek bir bildirimi okundu olarak işaretle
    @Operation(summary = "Belirli bir bildirimi okundu olarak işaretle")
    @PatchMapping("/{notificationId}/read")
    fun markAsRead(
        @PathVariable notificationId: Long
    ): ResponseEntity<Map<String, String>> {
        notificationService.markAsRead(notificationId)
        return ResponseEntity.ok(mapOf("message" to "Bildirim okundu olarak işaretlendi."))
    }

    // ✅ Tüm bildirimleri okundu olarak işaretle
    @Operation(summary = "Tüm bildirimleri okundu olarak işaretle")
    @PatchMapping("/read-all")
    fun markAllAsRead(): ResponseEntity<Map<String, String>> {
        notificationService.markAllAsRead()
        return ResponseEntity.ok(mapOf("message" to "Tüm bildirimler okundu olarak işaretlendi."))
    }

    // ✅ Okunmamış bildirim sayısını getir
    @Operation(summary = "Okunmamış bildirim sayısını getir")
    @GetMapping("/unread/count")
    fun getUnreadCount(): ResponseEntity<Map<String, Long>> {
        val count = notificationService.getUnreadCount()
        return ResponseEntity.ok(mapOf("unreadCount" to count))
    }
}
