package fr.paugesjanes.controllers

import fr.paugesjanes.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

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

    @GetMapping("/profil")
    fun profil(): String = "main/profil"
}