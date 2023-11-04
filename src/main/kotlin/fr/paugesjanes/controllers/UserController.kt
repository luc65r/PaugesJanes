package fr.paugesjanes.controllers

import fr.paugesjanes.repositories.ProjectRepository
import fr.paugesjanes.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException
import java.security.Principal

@Controller
@RequestMapping("/user")
class UserController(
    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val projectRepository: ProjectRepository,
) {
    @GetMapping("/{username}")
    fun showUser(
        @PathVariable
        username: String,
        principal: Principal?,
        model: Model,
    ): String {
        val user = userRepository.findByUsername(username) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        model["user"] = user
        model["isOwner"] = principal?.name == username
        return "user/show"
    }
}