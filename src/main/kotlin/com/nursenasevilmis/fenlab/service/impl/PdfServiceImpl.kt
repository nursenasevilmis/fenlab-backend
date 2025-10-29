package com.nursenasevilmis.fenlab.service.impl

import com.nursenasevilmis.fenlab.exception.ResourceNotFoundException
import com.nursenasevilmis.fenlab.model.PdfDownload
import com.nursenasevilmis.fenlab.repository.ExperimentRepository
import com.nursenasevilmis.fenlab.repository.PdfDownloadRepository
import com.nursenasevilmis.fenlab.repository.UserRepository
import com.nursenasevilmis.fenlab.service.PdfExportService
import com.nursenasevilmis.fenlab.service.PdfService
import com.nursenasevilmis.fenlab.util.SecurityUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.time.LocalDateTime

@Service
class PdfServiceImpl(
    private val pdfExportService: PdfExportService,
    private val experimentRepository: ExperimentRepository,
    private val pdfDownloadRepository: PdfDownloadRepository,
    private val userRepository: UserRepository
) : PdfService {

    override fun generateExperimentPdf(experimentId: Long): String {
        val experiment = experimentRepository.findById(experimentId)
            .orElseThrow { ResourceNotFoundException("Deney bulunamadı: $experimentId") }

        val pdfUrl = pdfExportService.generateExperimentPdf(experiment)

        // PDF indirme kaydını oluştur
        recordPdfDownload(experimentId)

        return pdfUrl
    }

    override fun generateExperimentPdfStream(experimentId: Long): InputStream {
        val experiment = experimentRepository.findById(experimentId)
            .orElseThrow { ResourceNotFoundException("Deney bulunamadı: $experimentId") }

        val pdfStream = pdfExportService.generateExperimentPdfStream(experiment)

        // PDF indirme kaydını oluştur
        recordPdfDownload(experimentId)

        return pdfStream
    }

    @Transactional
    override fun recordPdfDownload(experimentId: Long) {
        val currentUserId = SecurityUtils.getCurrentUserId()

        val experiment = experimentRepository.findById(experimentId)
            .orElseThrow { ResourceNotFoundException("Deney bulunamadı: $experimentId") }

        val user = userRepository.findById(currentUserId)
            .orElseThrow { ResourceNotFoundException("Kullanıcı bulunamadı") }

        // Kullanıcı bu PDF'i daha önce indirmiş mi kontrol et
        val existingRecord = pdfDownloadRepository.findByUserIdAndExperimentId(currentUserId, experimentId)

        if (existingRecord != null) {
            // Sadece tarihi güncelle
            existingRecord.downloadedAt = LocalDateTime.now()
            pdfDownloadRepository.save(existingRecord)
        } else {
            // Yeni kayıt oluştur
            val pdfDownload = PdfDownload(
                user = user,
                experiment = experiment,
                downloadedAt = LocalDateTime.now()
            )
            pdfDownloadRepository.save(pdfDownload)
        }
    }

}