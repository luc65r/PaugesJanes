package fr.paugesjanes.controllers

import fr.paugesjanes.entities.Project
import fr.paugesjanes.entities.User
import fr.paugesjanes.htmx
import fr.paugesjanes.repositories.ProjectRepository
import fr.paugesjanes.repositories.UserRepository
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/search")
class SearchController(
    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val projectRepository: ProjectRepository,
) {
    fun searchUser(query: String): List<User> =
        userRepository.findDistinctByUsernameContainsOrFullNameContainsAllIgnoreCase(query, query)

    fun searchProject(query: String): List<Project> =
        projectRepository.findDistinctByTitleContainsOrSummaryContainsAllIgnoreCase(query, query)

    @GetMapping("/user")
    fun getSearchUser(
        @RequestParam(required = false)
        q: String?,
        model: Model,
    ): String {
        model["users"] = if (q != null) searchUser(q) else listOf()
        return "search/user"
    }

    @HxRequest
    @GetMapping("/user")
    fun getSearchUserHx(
        @RequestParam
        q: String,
        model: Model,
    ): HtmxResponse {
        model["users"] = searchUser(q)
        return htmx { view("fragments/search :: users") }
    }

    @GetMapping("/project")
    fun getSearchProject(
        @RequestParam(required = false)
        q: String?,
        model: Model,
    ): String {
        model["projects"] = if (q != null) searchProject(q) else listOf()
        return "search/project"
    }

    @HxRequest
    @GetMapping("/project")
    fun getSearchProjectHx(
        @RequestParam
        q: String,
        model: Model,
    ): HtmxResponse {
        model["projects"] = searchProject(q)
        return htmx { view("fragments/search :: projects") }
    }
}