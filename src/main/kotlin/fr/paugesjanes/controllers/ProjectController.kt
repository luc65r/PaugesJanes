package fr.paugesjanes.controllers

import fr.paugesjanes.entities.Project
import fr.paugesjanes.htmx
import fr.paugesjanes.repositories.ProjectRepository
import fr.paugesjanes.repositories.UserRepository
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.security.Principal

@Controller
@RequestMapping("/project")
class ProjectController(
    @Autowired
    val projectRepository: ProjectRepository,

    @Autowired
    val userRepository: UserRepository,
) {
    data class ProjectInfo(
        @field:NotNull
        @field:Size(min = 2, max = 255)
        val title: String? = null,

        @field:NotNull
        @field:Size(min = 8, max = 255)
        @field:URL
        val link: String? = null,

        @field:Size(max = 255)
        val summary: String? = null,

        val description: String? = null,
    )

    @GetMapping("/create")
    fun createProject(model: Model): String {
        model["projectInfo"] = ProjectInfo()
        model["creating"] = true
        return "project/project"
    }

    @HxRequest
    @PostMapping("/create")
    fun postCreateProject(
        @Valid
        projectInfo: ProjectInfo,
        bindingResult: BindingResult,
        response: HttpServletResponse,
        principal: Principal,
        model: Model,
    ): HtmxResponse {
        model["projectInfo"] = projectInfo
        model["creating"] = true
        if (bindingResult.hasErrors())
            return htmx { view("fragments/project :: create") }

        val project = Project(
            projectInfo.title!!,
            projectInfo.link!!,
            projectInfo.summary,
        )
        val user = userRepository.findByUsername(principal.name)!!
        user.projects.add(project)
        project.authors.add(user)
        projectRepository.save(project)
        userRepository.save(user)

        return htmx { redirect("/project/${project.id}") }
    }

    @GetMapping("/{project}")
    fun showProject(
        @PathVariable
        project: Project,
        model: Model,
        principal: Principal?,
    ): String {
        model["isAuthor"] = if (principal != null) {
            val user = userRepository.findByUsername(principal.name)
            project.authors.contains(user)
        } else {
            false
        }
        model["project"] = project
        return "project/project"
    }

    @GetMapping("/{project}/edit")
    fun editProject(
        @PathVariable
        project: Project,
        model: Model,
    ): String {
        model["project"] = project
        model["projectInfo"] = ProjectInfo(
            project.title,
            project.link,
            project.summary,
        )
        return "fragments/project :: edit"
    }

    @PutMapping("/{project}")
    fun putProject(
        @PathVariable
        project: Project,
        @Valid
        projectInfo: ProjectInfo,
        bindingResult: BindingResult,
        model: Model,
        principal: Principal,
    ): String {
        val user = userRepository.findByUsername(principal.name)!!
        if (!project.authors.contains(user))
            throw ResponseStatusException(HttpStatus.FORBIDDEN);

        model["project"] = project
        if (bindingResult.hasErrors()) {
            model["projectInfo"] = projectInfo
            return "fragments/project :: edit"
        }

        projectRepository.save(project.apply {
            title = projectInfo.title!!
            link = projectInfo.link!!
            summary = projectInfo.summary
        })
        return "fragments/project :: show"
    }

    @HxRequest
    @DeleteMapping("/{project}")
    fun deleteProject(
        @PathVariable
        project: Project,
        principal: Principal,
    ): HtmxResponse {
        val user = userRepository.findByUsername(principal.name)!!
        if (!project.authors.contains(user))
            throw ResponseStatusException(HttpStatus.FORBIDDEN);

        for (u in project.authors) {
            u.projects.remove(project)
            userRepository.save(u)
        }

        projectRepository.delete(project)
        return htmx { redirect("/user/${user.username}") }
    }

    @HxRequest
    @PutMapping("/{project}/quit")
    fun quitProject(
        @PathVariable
        project: Project,
        principal: Principal,
    ): HtmxResponse {
        val user = userRepository.findByUsername(principal.name)!!
        user.projects.remove(project)
        project.authors.remove(user)
        userRepository.save(user)
        if (project.authors.isEmpty())
            projectRepository.delete(project)
        return htmx { redirect("/user/${user.username}") }
    }

    @HxRequest
    @PutMapping("/{project}/favorite")
    fun favoriteProject(
        @PathVariable project: Project,
        principal: Principal
    ): HtmxResponse {
        val user = userRepository.findByUsername(principal.name)!!

        // Vérifiez si le projet est déjà dans la liste des projets favoris de l'utilisateur
        if (!user.favorite.contains(project)) {
            // Si le projet n'est pas déjà en favori, ajoutez-le à la liste des favoris
            user.favorite.add(project)
            userRepository.save(user)
        }

        return htmx { redirect("/user/${user.username}") }
    }
}
