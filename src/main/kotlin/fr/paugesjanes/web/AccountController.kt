package fr.paugesjanes.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AccountController {
    @GetMapping("/login")
    fun login(): String = "account/login"

    @GetMapping("/logout")
    fun logout(): String = "account/logout"
}