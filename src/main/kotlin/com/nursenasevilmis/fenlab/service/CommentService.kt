package com.nursenasevilmis.fenlab.service
import com.nursenasevilmis.fenlab.dto.request.CommentCreateRequestDTO;
import com.nursenasevilmis.fenlab.dto.request.CommentUpdateRequestDTO
import com.nursenasevilmis.fenlab.dto.response.CommentResponseDTO;
import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO;

interface CommentService {
    fun getExperimentComments(experimentId: Long, page: Int, size: Int): PaginatedResponseDTO<CommentResponseDTO>
    fun addComment(experimentId: Long, request:CommentCreateRequestDTO): CommentResponseDTO
    fun updateComment(commentId: Long, request: CommentUpdateRequestDTO): CommentResponseDTO
    fun deleteComment(commentId: Long)
}