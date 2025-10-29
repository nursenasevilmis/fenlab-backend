package com.nursenasevilmis.fenlab.mapper

import com.nursenasevilmis.fenlab.dto.response.CommentResponseDTO
import com.nursenasevilmis.fenlab.model.Comment
import org.springframework.stereotype.Component


@Component
class CommentMapper(
    private val userMapper: UserMapper
) {

    fun toCommentResponse(comment: Comment, currentUserId: Long?): CommentResponseDTO {
        return CommentResponseDTO(
            id = comment.id!!,
            user = userMapper.toUserSummary(comment.user),
            content = comment.content,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt,
            isOwner = comment.user.id == currentUserId
        )
    }
}