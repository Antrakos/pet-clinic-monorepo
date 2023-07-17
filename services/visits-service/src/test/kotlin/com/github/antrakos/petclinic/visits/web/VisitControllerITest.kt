package com.github.antrakos.petclinic.visits.web

import com.github.antrakos.petclinic.visits.IntegrationTest
import com.github.antrakos.petclinic.visits.model.Visit
import com.github.antrakos.petclinic.visits.repository.VisitRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.time.Instant
import java.util.UUID

@IntegrationTest
class VisitControllerITest(
    private val webTestClient: WebTestClient,
    private val visitRepository: VisitRepository,
) {
    @BeforeEach
    @AfterAll
    fun cleanup() {
        runBlocking { visitRepository.deleteAll().awaitFirstOrNull() }
    }

    @Test
    fun `test get all visits`() {
        val visits = runBlocking {
            listOf(
                visitRepository.save(Visit(description = "Visit 1", petId = "pet-id-1")).awaitSingle(),
                visitRepository.save(Visit(description = "Visit 2", petId = "pet-id-2")).awaitSingle(),
            )
        }

        webTestClient.get()
            .uri("/api/v1/visits")
            .exchange()
            .expectStatus().isOk
            .expectBody<List<Visit>>()
            .isEqualTo(visits)
    }

    @Test
    fun `test get visit by id`() {
        val visit = runBlocking {
            visitRepository.save(Visit(description = "Visit description", petId = "pet-id-1")).awaitSingle()
        }

        webTestClient.get()
            .uri("/api/v1/visits/{id}", visit.id)
            .exchange()
            .expectStatus().isOk
            .expectBody<Visit>()
            .isEqualTo(visit)
    }

    @Test
    fun `test get visit by id not found`() {
        webTestClient.get()
            .uri("/api/v1/visits/{id}", UUID.randomUUID())
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `test create visit`() {
        val visitDto = ModifyVisitDto(
            date = Instant.now(),
            description = "Visit description",
            petId = "pet-id-1"
        )

        webTestClient.post()
            .uri("/api/v1/visits")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(visitDto)
            .exchange()
            .expectStatus().isCreated
            .expectHeader().valueMatches("Location", "/api/v1/visits/.*")
            .expectBody<Visit>()
            .value {
                assertThat(it)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(visitDto)
            }
    }

    @Test
    fun `test update visit`() {
        val visitDto = ModifyVisitDto(
            date = Instant.now(),
            description = "Updated visit description",
            petId = "pet-id-1"
        )

        val visit = runBlocking {
            visitRepository.save(Visit(description = "Initial description", petId = "pet-id-1")).awaitSingle()
        }

        webTestClient.put()
            .uri("/api/v1/visits/{id}", visit.id)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(visitDto)
            .exchange()
            .expectStatus().isOk
            .expectBody<Visit>()
            .value {
                assertThat(it)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(visitDto)
            }
    }

    @Test
    fun `test update visit not found`() {
        webTestClient.put()
            .uri("/api/v1/visits/{id}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ModifyVisitDto(date = Instant.now(), description = "Updated visit description", petId = "pet-id-1"))
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `test delete visit`() {
        val visit = runBlocking {
            visitRepository.save(Visit(petId = "pet-id-1")).awaitSingle()
        }

        webTestClient.delete()
            .uri("/api/v1/visits/{id}", visit.id)
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `test delete visit not found`() {
        webTestClient.delete()
            .uri("/api/v1/visits/{id}", UUID.randomUUID())
            .exchange()
            .expectStatus().isNotFound
    }
}
