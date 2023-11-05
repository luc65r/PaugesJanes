package fr.paugesjanes.repositories

import fr.paugesjanes.entities.Project
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProjectRepository : JpaRepository<Project, UUID> {
    fun findDistinctByTitleContainsOrSummaryContainsAllIgnoreCase(title: String, description: String): List<Project>
}