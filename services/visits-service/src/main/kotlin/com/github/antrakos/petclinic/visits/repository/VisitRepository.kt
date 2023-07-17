package com.github.antrakos.petclinic.visits.repository

import com.github.antrakos.petclinic.visits.model.Visit
import org.springframework.data.r2dbc.repository.R2dbcRepository
import java.util.UUID

interface VisitRepository : R2dbcRepository<Visit, UUID>
