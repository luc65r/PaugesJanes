package fr.paugesjanes.web

import fr.paugesjanes.User
import fr.paugesjanes.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.MergedAnnotations.Search
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

    @GetMapping("/search")
    fun search(model: Model): String {
        model.addAttribute("users", emptyList<User>()) // Initialize users as an empty list
        return "main/search"
    }

    @PostMapping("/search")
    @ResponseBody
    fun searchProfile(@RequestParam("search") search: String): List<User> {
        // Modify your repository method to search by username, portfolio, or project
        val usersByUsername = userRepository.findByUsernameContaining(search)
        val usersByPortfolio = userRepository.findByPortfolioContaining(search)

        // Combine the results and remove duplicates if necessary
        val searchResults = usersByUsername + usersByPortfolio
        return searchResults.distinct()
    }


}