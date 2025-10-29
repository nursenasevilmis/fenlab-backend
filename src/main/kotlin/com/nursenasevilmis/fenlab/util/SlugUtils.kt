package com.nursenasevilmis.fenlab.util

import java.text.Normalizer

object SlugUtils {

    /**
     * String'i URL-friendly slug'a çevirir
     */
    fun toSlug(input: String): String {
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        return normalized
            .replace("[^\\p{ASCII}]".toRegex(), "")
            .lowercase()
            .replace("[^a-z0-9\\s-]".toRegex(), "")
            .trim()
            .replace("\\s+".toRegex(), "-")
            .replace("-+".toRegex(), "-")
    }

    /**
     * Türkçe karakterleri İngilizce'ye çevirir
     */
    fun turkishToEnglish(input: String): String {
        return input
            .replace('ı', 'i')
            .replace('İ', 'I')
            .replace('ğ', 'g')
            .replace('Ğ', 'G')
            .replace('ü', 'u')
            .replace('Ü', 'U')
            .replace('ş', 's')
            .replace('Ş', 'S')
            .replace('ö', 'o')
            .replace('Ö', 'O')
            .replace('ç', 'c')
            .replace('Ç', 'C')
    }
}