package fr.paugesjanes.entities

import jakarta.persistence.*
import java.util.*

@Entity
class Project(
    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var link: String,

    var summary: String? = null,

    @Id
    @Column(nullable = false)
    val id: UUID = UUID.randomUUID(),
) {
    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
    var authors: MutableSet<User> = mutableSetOf()

    @ManyToMany
    var tags: MutableSet<Tag> = mutableSetOf()
}
