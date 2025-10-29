package com.nursenasevilmis.fenlab.util

import com.nursenasevilmis.fenlab.security.UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {

    /**
     * Mevcut kullanıcının ID'sini döndürür
     */
    fun getCurrentUserId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication.principal as? UserPrincipal
            ?: throw IllegalStateException("User not authenticated")
        return principal.id
    }

    /**
     * Mevcut kullanıcının UserPrincipal nesnesini döndürür
     */
    fun getCurrentUser(): UserPrincipal {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as? UserPrincipal
            ?: throw IllegalStateException("User not authenticated")
    }

    /**
     * Kullanıcının öğretmen olup olmadığını kontrol eder
     */
    fun isTeacher(): Boolean {
        return try {
            val user = getCurrentUser()
            user.role.name == "TEACHER"
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Kullanıcının giriş yapıp yapmadığını kontrol eder
     */
    fun isAuthenticated(): Boolean {
        return try {
            getCurrentUserId()
            true
        } catch (e: Exception) {
            false
        }
    }
}
