package fr.paugesjanes

import fr.paugesjanes.entities.Project
import fr.paugesjanes.repositories.ProjectRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
@AutoConfigureTestDatabase
class ProjectRepositoryTest(
    @Autowired
    val projectRepository: ProjectRepository,
) {
    @Test
    fun `insert project`() {
        projectRepository.save(Project(title = "", link = ""))
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class `when one project`() {
        private val id = UUID.randomUUID()

        init {
            projectRepository.save(Project(id, title = "", link = ""))
        }

        @Test
        fun `find project`() {
            val project = projectRepository.findById(id)
            assertTrue(project.isPresent)
        }
    }
}