package fr.paugesjanes.constraints

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.springframework.beans.BeanUtils
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldMatch(
    val otherField: String,
    val message: String,
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [FieldMatchValidator::class])
annotation class EnableFieldMatchConstraint(
    val message: String = "",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

class FieldMatchValidator : ConstraintValidator<EnableFieldMatchConstraint, Any> {
    override fun isValid(o: Any?, context: ConstraintValidatorContext?): Boolean {
        if (o == null)
            return true

        for (field in o.javaClass.declaredFields) {
            if (field.isAnnotationPresent(FieldMatch::class.java)) {
                val mainField = field.name
                val otherField = field.getAnnotation(FieldMatch::class.java).otherField
                val message = field.getAnnotation(FieldMatch::class.java).message

                val mainObj = BeanUtils
                    .getPropertyDescriptor(o.javaClass, mainField)!!
                    .readMethod
                    .invoke(o)
                val otherObj = BeanUtils
                    .getPropertyDescriptor(o.javaClass, otherField)!!
                    .readMethod
                    .invoke(o)
                if (mainObj != otherObj) {
                    context?.disableDefaultConstraintViolation()
                    context
                        ?.buildConstraintViolationWithTemplate(message)
                        ?.addPropertyNode(mainField)
                        ?.addConstraintViolation()
                    return false
                }
            }
        }

        return true
    }
}