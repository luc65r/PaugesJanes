package fr.paugesjanes.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {
    @GetMapping("/login")
    fun login(): String = "login"

    @GetMapping("/logout")
    fun logout(): String = "logout"
}