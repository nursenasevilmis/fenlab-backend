package com.nursenasevilmis.fenlab.controller

import com.nursenasevilmis.fenlab.dto.request.CommentCreateRequestDTO
import com.nursenasevilmis.fenlab.dto.request.CommentUpdateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.CommentResponseDTO
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import com.nursenasevilmis.fenlab.service.CommentService
import com.nursenasevilmis.fenlab.util.SecurityUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService
) {

    // 1️⃣ Deneye göre tüm yorumları listele
    @GetMapping("/experiment/{experimentId}")
    fun getExperimentComments(
        @PathVariable experimentId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponseDTO<CommentResponseDTO>> {
        val comments = commentService.getExperimentComments(experimentId, page, size)
        return ResponseEntity.ok(comments)
    }

    // 2️⃣ Kullanıcının kendi deneyindeki yorumları (profil -> deneylerim)
    @GetMapping("/my-experiments/{experimentId}")
    fun getMyExperimentComments(
        @PathVariable experimentId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponseDTO<CommentResponseDTO>> {
        val currentUserId = SecurityUtils.getCurrentUserId()
        val allComments = commentService.getExperimentComments(experimentId, page, size)
        val myComments = allComments.copy(
            content = allComments.content.filter { it.isOwner } // isOwner DTO alanı sayesinde filtreleme
        )
        return ResponseEntity.ok(myComments)
    }

    // 3️⃣ Yorum ekleme
    @PostMapping("/experiment/{experimentId}")
    fun addComment(
        @PathVariable experimentId: Long,
        @Valid @RequestBody request: CommentCreateRequestDTO
    ): ResponseEntity<CommentResponseDTO> {
        val createdComment = commentService.addComment(experimentId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment)
    }

    // 4️⃣ Yorum güncelleme (sadece kendi yorumuna)
    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @Valid @RequestBody request: CommentUpdateRequestDTO
    ): ResponseEntity<CommentResponseDTO> {
        val updatedComment = commentService.updateComment(commentId, request)
        return ResponseEntity.ok(updatedComment)
    }

    // 5️⃣ Yorum silme (sadece kendi yorumuna veya kendi deneyindeki yorumlara)
    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Long): ResponseEntity<Void> {
        commentService.deleteComment(commentId)
        return ResponseEntity.noContent().build()
    }
}
