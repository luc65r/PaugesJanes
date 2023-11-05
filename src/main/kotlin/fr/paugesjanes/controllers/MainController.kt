package fr.paugesjanes.controllers

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
import java.security.Principal

@Controller
class MainController(
    @Autowired
    var userRepository: UserRepository, private val projectRepository: ProjectRepository
) {
    @GetMapping("/")
    fun index(model: Model): String {
        model["portfolio"] = projectRepository.findRandomPortfolio()
        return "main/index"
    }

    @HxRequest
    @GetMapping("/getRandomPortfolio")
    fun getRandomPortfolio(model: Model): HtmxResponse {
        model["portfolio"] = projectRepository.findRandomPortfolio()
        return htmx { view("fragments/homeCard :: homeCard") }
    }

    @GetMapping("/a_propos")
    fun aPropos(): String = "main/a_propos"

    @GetMapping("/contact")
    fun contact(): String = "main/contact"

    @GetMapping("/favorite")
    fun favorite(
        model: Model,
        principal: Principal,
    ): String {
        val user = userRepository.findByUsername(principal.name)!!
        model["projects"] = user.favorite
        return "main/favorite"
    }
}