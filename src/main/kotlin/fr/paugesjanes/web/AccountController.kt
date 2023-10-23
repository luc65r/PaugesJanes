package fr.paugesjanes.web

import fr.paugesjanes.User
import fr.paugesjanes.UserRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping


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
}

data class RegistrationInfo(
    @field:NotNull
    @field:Size(min = 2, max = 32)
    val username: String? = null,

    @field:NotNull
    @field:Size(min = 8, max = 64)
    val password: String? = null,

    @field:NotNull
    var passwordConfirmation: String? = null,

    @field:NotNull
    @field:Email
    val email: String? = null,

    @field:NotNull
    @field:Size(min = 1, max = 64)
    val fullName: String? = null,
)
