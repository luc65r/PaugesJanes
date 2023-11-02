package fr.paugesjanes.entities

import jakarta.persistence.*
import java.util.*

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
