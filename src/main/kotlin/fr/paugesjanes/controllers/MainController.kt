package fr.paugesjanes.controllers

import fr.paugesjanes.entities.User
import fr.paugesjanes.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class MainController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("/")
    fun index(model: Model): String = "main/index"

    @GetMapping("/a_propos")
    fun aPropos(): String = "main/a_propos"

    @GetMapping("/contact")
    fun contact(): String = "main/contact"

    @GetMapping("/profile")
    fun profile(): String = "main/profile"

    @GetMapping("/search")
    fun search(model: Model): String {
        model.addAttribute("users", emptyList<User>()) // Initialize users as an empty list
        return "main/search"
    }

    @PostMapping("/search")
    @ResponseBody
    fun searchProfile(@RequestParam("search") search: String): List<User> {
        val usersByUsername = userRepository.findByUsernameContaining(search)
        val usersByPortfolio = userRepository.findByPortfolioContaining(search)

        val searchResults = usersByUsername + usersByPortfolio
        return searchResults.distinct()
    }


}