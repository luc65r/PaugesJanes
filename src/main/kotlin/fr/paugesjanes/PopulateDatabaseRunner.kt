package fr.paugesjanes

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PopulateDatabaseRunner(
    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val roleRepository: RoleRepository,

    @Autowired
    val passwordEncoder: PasswordEncoder,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
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