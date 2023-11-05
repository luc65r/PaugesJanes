package fr.paugesjanes.entities

import jakarta.persistence.*
import java.util.*

@Entity
class User(
    @Column(unique = true, nullable = false)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column(nullable = false)
    var fullName: String,

    @Id
    @Column(nullable = false)
    val id: UUID = UUID.randomUUID(),
) {
    @ManyToMany
    var roles: MutableSet<Role> = mutableSetOf()

    @ManyToOne
    var portfolio: Project? = null

    @ManyToMany(fetch = FetchType.LAZY)
    var projects: MutableSet<Project> = mutableSetOf()

    @ManyToMany
    var favorite: MutableSet<Project> = mutableSetOf()
}