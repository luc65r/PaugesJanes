package fr.paugesjanes

import fr.paugesjanes.entities.Role
import fr.paugesjanes.entities.User
import fr.paugesjanes.repositories.RoleRepository
import fr.paugesjanes.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
@DependsOnDatabaseInitialization
class PopulateDatabaseRunner(
    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val roleRepository: RoleRepository,

    @Autowired
    val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        if (!roleRepository.existsById("ADMIN"))
            roleRepository.save(Role("ADMIN"))

        if (!userRepository.existsByUsername("admin")) {
            val admin = User(
                username = "admin",
                password = passwordEncoder.encode("admin"),
                email = "admin@paugesjanes.fr",
                fullName = "Admine Histrateur",
            )
            admin.roles.add(Role("ADMIN"))
            userRepository.save(admin)
        }
    }
}