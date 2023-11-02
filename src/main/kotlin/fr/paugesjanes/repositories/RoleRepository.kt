package fr.paugesjanes.repositories

import fr.paugesjanes.entities.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String>