package com.nursenasevilmis.fenlab.service

import java.io.InputStream

interface PdfService {
    fun generateExperimentPdf(experimentId: Long): String
    fun generateExperimentPdfStream(experimentId: Long): InputStream
    fun recordPdfDownload(experimentId: Long)
}