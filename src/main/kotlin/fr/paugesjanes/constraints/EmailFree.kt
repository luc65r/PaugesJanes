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
@Constraint(validatedBy = [EmailFreeValidator::class])
annotation class EmailFree(
    val message: String = "Email already used",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

class EmailFreeValidator(
    @Autowired
    val userRepository: UserRepository,
) : ConstraintValidator<EmailFree, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null)
            return true

        return !userRepository.existsByEmail(value)
    }
}