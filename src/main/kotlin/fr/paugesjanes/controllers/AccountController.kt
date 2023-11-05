package fr.paugesjanes.controllers

import fr.paugesjanes.constraints.EmailFree
import fr.paugesjanes.constraints.EnableFieldMatchConstraint
import fr.paugesjanes.constraints.FieldMatch
import fr.paugesjanes.constraints.UsernameFree
import fr.paugesjanes.entities.Project
import fr.paugesjanes.entities.User
import fr.paugesjanes.htmx
import fr.paugesjanes.repositories.UserRepository
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import java.security.Principal


@Controller
class AccountController(
    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val passwordEncoder: PasswordEncoder,
) {
    @GetMapping("/login")
    fun login(): String = "account/login"

    @GetMapping("/logout")
    fun logout(): String = "account/logout"

    @EnableFieldMatchConstraint
    data class RegistrationInfo(
        @field:NotNull
        @field:Size(min = 2, max = 32, message = "Le nom d’utilisateur doit être composé de 2 à 32 caractères")
        @UsernameFree(message = "Nom d’utilisateur non disponible")
        val username: String? = null,

        @field:NotNull
        @field:Size(min = 8, max = 64, message = "Le mot de passe doit être composé de 8 à 64 caractères")
        val password: String? = null,

        @field:NotNull
        @FieldMatch(otherField = "password", message = "La confirmation ne correspond pas au mot de passe")
        var passwordConfirmation: String? = null,

        @field:NotNull
        @field:Email(message = "Adresse e-mail non valide")
        @EmailFree(message = "Adresse e-mail non disponible")
        val email: String? = null,

        @field:NotNull
        @field:Size(min = 2, max = 64, message = "Le nom complet doit être composé de 2 à 32 caractères")
        val fullName: String? = null,
    )

    @GetMapping("/register")
    fun register(model: Model): String {
        model["registrationInfo"] = RegistrationInfo()
        return "account/register"
    }

    @PostMapping("/register")
    fun postRegister(
        @Valid
        registrationInfo: RegistrationInfo,
        bindingResult: BindingResult,
        model: Model,
    ): String {
        model["registrationInfo"] = registrationInfo
        if (bindingResult.hasErrors())
            return "account/register"

        userRepository.save(
            User(
                registrationInfo.username!!,
                passwordEncoder.encode(registrationInfo.password!!),
                registrationInfo.email!!,
                registrationInfo.fullName!!,
            )
        )
        return "account/confirmRegister"
    }

    @HxRequest
    @PutMapping("/setPortfolio")
    fun setPortfolio(
        @RequestParam
        project: Project,
        principal: Principal,
    ): HtmxResponse {
        val user = userRepository.findByUsername(principal.name)!!
        if (!project.authors.contains(user))
            throw ResponseStatusException(HttpStatus.FORBIDDEN)

        user.portfolio = project
        userRepository.save(user)

        return htmx { redirect("/user/${user.username}") }
    }
}