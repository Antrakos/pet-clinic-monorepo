package com.github.antrakos.petclinic.visits.web

import com.github.antrakos.petclinic.visits.model.Visit
import com.github.antrakos.petclinic.visits.repository.VisitRepository
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.time.Instant
import java.util.UUID

@Transactional
@RestController
@RequestMapping("/api/v1/visits")
class VisitController(private val visitRepository: VisitRepository) {
    @GetMapping
    suspend fun getAllVisits() = visitRepository.findAll().asFlow()


    @GetMapping("/{id}")
    suspend fun getVisitById(@PathVariable id: UUID): ResponseEntity<Visit> {
        val visit = visitRepository.findById(id).awaitSingleOrNull() ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(visit)
    }

    @PostMapping
    suspend fun createVisit(@RequestBody visit: ModifyVisitDto): ResponseEntity<Visit> {
        val savedVisit = visitRepository.save(visit.toModel()).awaitSingle()
        return ResponseEntity.created(URI.create("/api/v1/visits/" + savedVisit.id))
            .body(savedVisit)
    }

    @PutMapping("/{id}")
    suspend fun updateVisit(@PathVariable id: UUID, @RequestBody visit: ModifyVisitDto): ResponseEntity<Visit> {
        if (!visitRepository.existsById(id).awaitSingle()) {
            return ResponseEntity.notFound().build()
        }
        val updatedVisit = visitRepository.save(visit.toModel(id)).awaitSingle()
        return ResponseEntity.ok(updatedVisit)
    }

    @DeleteMapping("/{id}")
    suspend fun deleteVisit(@PathVariable id: UUID): ResponseEntity<Void> {
        if (!visitRepository.existsById(id).awaitSingle()) {
            return ResponseEntity.notFound().build()
        }
        visitRepository.deleteById(id).awaitFirstOrNull()
        return ResponseEntity.noContent().build()
    }
}

data class ModifyVisitDto(
    val petId: String,
    val description: String? = null,
    val date: Instant,
) {
    fun toModel(id: UUID? = null) = Visit(id, date, description, petId)
}
