package com.nursenasevilmis.fenlab.util

object ValidationUtils {

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    private val USERNAME_REGEX = "^[a-zA-Z0-9_]{3,50}$".toRegex()

    /**
     * Email geçerliliğini kontrol eder
     */
    fun isValidEmail(email: String): Boolean {
        return email.matches(EMAIL_REGEX)
    }

    /**
     * Username geçerliliğini kontrol eder
     */
    fun isValidUsername(username: String): Boolean {
        return username.matches(USERNAME_REGEX)
    }

    /**
     * Şifre güvenliğini kontrol eder
     */
    fun isStrongPassword(password: String): Boolean {
        if (password.length < 8) return false

        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }

        return hasUpperCase && hasLowerCase && hasDigit
    }

    /**
     * String'in boş olup olmadığını kontrol eder
     */
    fun isNotBlank(value: String?): Boolean {
        return !value.isNullOrBlank()
    }
}