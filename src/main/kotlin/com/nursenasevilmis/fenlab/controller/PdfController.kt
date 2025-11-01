package com.nursenasevilmis.fenlab.controller

import com.nursenasevilmis.fenlab.service.PdfService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/pdf")
@Tag(name = "PDF Operations", description = "Deney PDF oluşturma ve indirme işlemleri")
class PdfController(
    private val pdfService: PdfService
) {

    @GetMapping("/{experimentId}/generate")
    @Operation(summary = "PDF oluştur (URL döner)", description = "Belirtilen deneyin PDF çıktısını oluşturur ve dosya URL’sini döndürür.")
    fun generatePdf(@PathVariable experimentId: Long): ResponseEntity<Map<String, String>> {
        val pdfUrl = pdfService.generateExperimentPdf(experimentId)
        return ResponseEntity.ok(mapOf("pdfUrl" to pdfUrl))
    }

    @GetMapping("/{experimentId}/download")
    @Operation(summary = "PDF indir (dosya döner)", description = "Belirtilen deneyin PDF çıktısını oluşturur ve dosya olarak indirir.")
    fun downloadPdf(@PathVariable experimentId: Long): ResponseEntity<InputStreamResource> {
        val pdfStream = pdfService.generateExperimentPdfStream(experimentId)

        val headers = HttpHeaders().apply {
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"experiment_$experimentId.pdf\"")
        }

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(InputStreamResource(pdfStream))
    }
}
