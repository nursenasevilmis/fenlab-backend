package com.nursenasevilmis.fenlab.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateTimeUtils {

    private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    /**
     * LocalDateTime'ı string'e çevirir
     */
    fun formatDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(DATE_TIME_FORMATTER)
    }

    /**
     * LocalDateTime'ı tarih string'e çevirir
     */
    fun formatDate(dateTime: LocalDateTime?): String? {
        return dateTime?.format(DATE_FORMATTER)
    }

    /**
     * Göreceli zaman hesaplar (örn: "2 saat önce")
     */
    fun getRelativeTime(dateTime: LocalDateTime): String {
        val now = LocalDateTime.now()
        val minutes = ChronoUnit.MINUTES.between(dateTime, now)
        val hours = ChronoUnit.HOURS.between(dateTime, now)
        val days = ChronoUnit.DAYS.between(dateTime, now)

        return when {
            minutes < 1 -> "Az önce"
            minutes < 60 -> "$minutes dakika önce"
            hours < 24 -> "$hours saat önce"
            days < 7 -> "$days gün önce"
            days < 30 -> "${days / 7} hafta önce"
            days < 365 -> "${days / 30} ay önce"
            else -> "${days / 365} yıl önce"
        }
    }
}