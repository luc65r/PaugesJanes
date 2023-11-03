package fr.paugesjanes.web

import fr.paugesjanes.entities.Project
import fr.paugesjanes.repositories.ProjectRepository
import fr.paugesjanes.repositories.UserRepository
import fr.paugesjanes.services.MarkdownService
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

inline fun htmx(buildHtmxResponse: HtmxResponse.Builder.() -> Unit): HtmxResponse {
    val builder = HtmxResponse.Builder()
    builder.buildHtmxResponse()
    return builder.build()
}

@Controller
@RequestMapping("/project")
class ProjectController(
    @Autowired
    val projectRepository: ProjectRepository,

    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val markdownService: MarkdownService,
) {
    data class ProjectInfo(
        @field:NotNull
        @field:Size(min = 2, max = 255)
        val title: String? = null,

        @field:NotNull
        @field:Size(min = 8, max = 255)
        @field:URL
        val link: String? = null,

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

        val project = projectRepository.save(
            Project(
                projectInfo.title!!,
                projectInfo.link!!,
                projectInfo.description,
            )
        )
        val user = userRepository.findByUsername(principal.name)!!
        project.authors.add(user)
        return htmx { redirect("/project/${project.id}") }
    }

    @GetMapping("/{project}")
    fun showProject(
        @PathVariable
        project: Project,
        model: Model,
    ): String {
        model["project"] = project
        project.description?.let {
            model["renderedDescription"] = markdownService.markdownToHtml(it)
        }
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
            project.description,
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
    ): String {
        model["project"] = project
        if (bindingResult.hasErrors()) {
            model["projectInfo"] = projectInfo
            return "fragments/project :: edit"
        }

        projectRepository.save(project.apply {
            title = projectInfo.title!!
            link = projectInfo.link!!
            description = projectInfo.description
        })
        project.description?.let {
            model["renderedDescription"] = markdownService.markdownToHtml(it)
        }
        return "fragments/project :: show"
    }

    @DeleteMapping("/{project}")
    fun deleteProject(
        @PathVariable
        project: Project,
        principal: Principal,
    ): String {
        val user = userRepository.findByUsername(principal.name)!!
        if (!project.authors.contains(user))
            throw ResponseStatusException(HttpStatus.FORBIDDEN);

        projectRepository.delete(project)
        return "redirect:/project"
    }

    @PutMapping("/{project}/quit")
    fun quitProject(
        @PathVariable
        project: Project,
        principal: Principal,
    ): String {
        val user = userRepository.findByUsername(principal.name)!!
        project.authors.remove(user)
        return "redirect:/user/${user.id}"
    }
}
