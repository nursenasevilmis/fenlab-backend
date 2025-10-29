package com.nursenasevilmis.fenlab.repository

import com.nursenasevilmis.fenlab.model.User
import com.nursenasevilmis.fenlab.model.enums.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User

    fun findByEmail(email: String): User

    fun findByUsernameOrEmail(username: String, email: String): User

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun findByIdAndIsDeletedFalse(id: Long): User

    fun findByRole(role: UserRole): List<User>

    @Query("SELECT COUNT(e) FROM Experiment e WHERE e.user.id = :userId AND e.isDeleted = false")
    fun countExperimentsByUserId(@Param("userId") userId: Long): Long
}