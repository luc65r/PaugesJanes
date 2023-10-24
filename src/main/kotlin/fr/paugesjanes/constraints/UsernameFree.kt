package fr.paugesjanes.constraints

import fr.paugesjanes.UserRepository
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.springframework.beans.factory.annotation.Autowired
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UsernameFreeValidator::class])
annotation class UsernameFree(
    val message: String = "Username already used",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

class UsernameFreeValidator(
    @Autowired
    val userRepository: UserRepository,
) : ConstraintValidator<UsernameFree, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null)
            return true

        return !userRepository.existsByUsername(value)
    }
}