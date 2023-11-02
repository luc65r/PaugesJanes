package fr.paugesjanes.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Tag(
    @Id
    @Column(nullable = false)
    val name: String,
)