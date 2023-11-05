package fr.paugesjanes.repositories

import fr.paugesjanes.entities.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ProjectRepository : JpaRepository<Project, UUID> {
    fun findDistinctByTitleContainsOrSummaryContainsAllIgnoreCase(title: String, description: String): List<Project>

    /* Crimes against humanity */
    @Query(nativeQuery = true, value = """
        SELECT * FROM (SELECT DISTINCT p.* FROM "project" AS p JOIN "user" AS u WHERE u."portfolio_id" = p."id") ORDER BY RAND() LIMIT 1
    """)
    fun findRandomPortfolio(): Project?
}