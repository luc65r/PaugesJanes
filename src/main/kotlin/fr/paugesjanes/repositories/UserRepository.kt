package fr.paugesjanes.repositories

import fr.paugesjanes.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun findByUsernameContaining(search: String): List<User>

    fun findByPortfolioContaining(search: String): List<User>
}