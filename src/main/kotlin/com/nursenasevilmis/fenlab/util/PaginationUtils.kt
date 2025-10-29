package com.nursenasevilmis.fenlab.util

import com.nursenasevilmis.fenlab.dto.response.PaginatedResponseDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

object PaginationUtils {

    private const val DEFAULT_PAGE_SIZE = 20
    private const val MAX_PAGE_SIZE = 100

    /**
     * Pageable oluşturur
     */
    fun createPageable(page: Int, size: Int, sort: Sort = Sort.unsorted()): Pageable {
        val validatedSize = size.coerceIn(1, MAX_PAGE_SIZE)
        val validatedPage = page.coerceAtLeast(0)
        return PageRequest.of(validatedPage, validatedSize, sort)
    }

    /**
     * Page nesnesini PaginatedResponse'a çevirir
     */
    fun <T> toPaginatedResponse(page: Page<T>): PaginatedResponseDTO<T> {
        return PaginatedResponseDTO(
            content = page.content,
            page = page.number,
            size = page.size,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            isFirst = page.isFirst,
            isLast = page.isLast
        )
    }
}