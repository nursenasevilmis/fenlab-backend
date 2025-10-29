package com.nursenasevilmis.fenlab.dto.response
//Herhangi bir liste yanıtını sayfalama ile döndürmek için generic yapı:Spring Pageable yapısına karşılık gelir.
data class PaginatedResponseDTO<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)
