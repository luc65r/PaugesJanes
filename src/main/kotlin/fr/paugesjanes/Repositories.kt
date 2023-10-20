package fr.paugesjanes

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean
}

interface RoleRepository : JpaRepository<Role, String>

interface ProjectRepository : JpaRepository<Project, UUID>

interface TagRepository : JpaRepository<Tag, String>