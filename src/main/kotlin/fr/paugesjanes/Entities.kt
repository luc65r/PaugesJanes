package fr.paugesjanes

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

    @ManyToMany
    var projects: MutableSet<Project> = mutableSetOf()
}

@Entity
class Role(
    @Id
    @Column(nullable = false)
    val name: String,
)

@Entity
class Project(
    @Id
    @Column(nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var link: String,
) {
    @Lob
    var description: String? = null

    @ManyToMany(mappedBy = "projects")
    var authors: MutableSet<User> = mutableSetOf()

    @ManyToMany
    var tags: MutableSet<Tag> = mutableSetOf()
}

@Entity
class Tag(
    @Id
    @Column(nullable = false)
    val name: String,
)