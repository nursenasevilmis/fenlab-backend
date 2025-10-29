package com.nursenasevilmis.fenlab.service

import com.itextpdf.html2pdf.HtmlConverter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import com.nursenasevilmis.fenlab.model.Experiment
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Service
class PdfExportService(
    private val minioService: MinioService
) {

    /**
     * Deney detaylarını PDF olarak oluşturur ve MinIO'ya yükler
     */
    fun generateExperimentPdf(experiment: Experiment): String {
        val htmlContent = generateExperimentHtml(experiment)
        val pdfBytes = convertHtmlToPdf(htmlContent)

        val fileName = "experiment_${experiment.id}_${System.currentTimeMillis()}.pdf"
        val inputStream = ByteArrayInputStream(pdfBytes)

        return minioService.uploadPdf(inputStream, fileName)
    }

    /**
     * Deney için HTML içerik oluşturur
     */
    private fun generateExperimentHtml(experiment: Experiment): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: 'Arial', sans-serif;
                        margin: 40px;
                        color: #333;
                    }
                    h1 {
                        color: #2c3e50;
                        border-bottom: 3px solid #3498db;
                        padding-bottom: 10px;
                    }
                    h2 {
                        color: #34495e;
                        margin-top: 30px;
                        border-left: 4px solid #3498db;
                        padding-left: 10px;
                    }
                    .info-section {
                        background-color: #ecf0f1;
                        padding: 15px;
                        border-radius: 5px;
                        margin: 20px 0;
                    }
                    .info-item {
                        margin: 10px 0;
                    }
                    .label {
                        font-weight: bold;
                        color: #2c3e50;
                    }
                    .materials, .steps {
                        margin: 20px 0;
                    }
                    .material-item, .step-item {
                        padding: 10px;
                        margin: 5px 0;
                        background-color: #f8f9fa;
                        border-left: 3px solid #3498db;
                    }
                    .step-number {
                        display: inline-block;
                        background-color: #3498db;
                        color: white;
                        padding: 5px 10px;
                        border-radius: 50%;
                        margin-right: 10px;
                        font-weight: bold;
                    }
                    .safety-note {
                        background-color: #ffe5e5;
                        border-left: 4px solid #e74c3c;
                        padding: 15px;
                        margin: 20px 0;
                    }
                    .footer {
                        margin-top: 50px;
                        text-align: center;
                        color: #7f8c8d;
                        font-size: 12px;
                        border-top: 1px solid #bdc3c7;
                        padding-top: 20px;
                    }
                </style>
            </head>
            <body>
                <h1>${experiment.title}</h1>
                
                <div class="info-section">
                    <div class="info-item">
                        <span class="label">Öğretmen:</span> ${experiment.user.fullName}
                    </div>
                    <div class="info-item">
                        <span class="label">Ders:</span> ${experiment.subject}
                    </div>
                    <div class="info-item">
                        <span class="label">Sınıf Seviyesi:</span> ${experiment.gradeLevel}. Sınıf
                    </div>
                    <div class="info-item">
                        <span class="label">Zorluk:</span> ${experiment.difficulty}
                    </div>
                </div>
                
                <h2>Deney Açıklaması</h2>
                <p>${experiment.description}</p>
                
                ${if (experiment.materials.isNotEmpty()) generateMaterialsHtml(experiment) else ""}
                
                ${if (experiment.steps.isNotEmpty()) generateStepsHtml(experiment) else ""}
                
                ${if (!experiment.expectedResult.isNullOrBlank()) """
                    <h2>Beklenen Sonuç</h2>
                    <p>${experiment.expectedResult}</p>
                """ else ""}
                
                ${if (!experiment.safetyNotes.isNullOrBlank()) """
                    <div class="safety-note">
                        <h2>⚠️ Güvenlik Notları</h2>
                        <p>${experiment.safetyNotes}</p>
                    </div>
                """ else ""}
                
                <div class="footer">
                    <p>Bu deney Fenlab platformunda paylaşılmıştır.</p>
                    <p>Oluşturulma Tarihi: ${experiment.createdAt}</p>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    private fun generateMaterialsHtml(experiment: Experiment): String {
        val materialsHtml = experiment.materials.joinToString("\n") { material ->
            """
                <div class="material-item">
                    <strong>${material.materialName}</strong>: ${material.quantity}
                </div>
            """
        }

        return """
            <h2>Gerekli Malzemeler</h2>
            <div class="materials">
                $materialsHtml
            </div>
        """
    }

    private fun generateStepsHtml(experiment: Experiment): String {
        val sortedSteps = experiment.steps.sortedBy { it.stepOrder }
        val stepsHtml = sortedSteps.joinToString("\n") { step ->
            """
                <div class="step-item">
                    <span class="step-number">${step.stepOrder}</span>
                    ${step.stepText}
                </div>
            """
        }

        return """
            <h2>Deney Adımları</h2>
            <div class="steps">
                $stepsHtml
            </div>
        """
    }

    /**
     * HTML'i PDF'e çevirir
     */
    private fun convertHtmlToPdf(htmlContent: String): ByteArray {
        val outputStream = ByteArrayOutputStream()

        try {
            HtmlConverter.convertToPdf(htmlContent, outputStream)
            return outputStream.toByteArray()
        } catch (e: Exception) {
            throw RuntimeException("PDF oluşturulurken hata oluştu: ${e.message}", e)
        } finally {
            outputStream.close()
        }
    }

    /**
     * PDF'i direkt indirilebilir InputStream olarak döndürür
     */
    fun generateExperimentPdfStream(experiment: Experiment): InputStream {
        val htmlContent = generateExperimentHtml(experiment)
        val pdfBytes = convertHtmlToPdf(htmlContent)
        return ByteArrayInputStream(pdfBytes)
    }
}