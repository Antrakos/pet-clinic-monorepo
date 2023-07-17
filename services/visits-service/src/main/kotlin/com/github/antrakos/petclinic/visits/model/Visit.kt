package com.github.antrakos.petclinic.visits.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Table("visits")
data class Visit(
    @Id val id: UUID? = null,
    val date: Instant = Instant.now().truncatedTo(ChronoUnit.SECONDS),
    val description: String? = null,
    @Column("pet_id") val petId: String,
)
