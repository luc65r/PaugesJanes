package fr.paugesjanes.controllers

import fr.paugesjanes.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

@Controller
class MainController(
    @Autowired
    var userRepository: UserRepository
) {
    @GetMapping("/")
    fun index(model: Model): String = "main/index"

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

    @GetMapping("/profil")
    fun profil(): String = "main/profil"
}