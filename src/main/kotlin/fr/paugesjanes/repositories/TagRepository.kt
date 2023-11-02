package fr.paugesjanes.repositories

import fr.paugesjanes.entities.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, String>