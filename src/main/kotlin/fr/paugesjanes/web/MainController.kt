package fr.paugesjanes.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {
    @GetMapping("/")
    fun index(model: Model): String = "main/index"

    @GetMapping("/a_propos")
    fun aPropos(): String = "main/a_propos"

    @GetMapping("/contact")
    fun contact(): String = "main/contact"

}