package com.github.antrakos.petclinic.vets.web;

import com.github.antrakos.petclinic.vets.IntegrationTest;
import com.github.antrakos.petclinic.vets.model.Vet;
import com.github.antrakos.petclinic.vets.repository.VetRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class VetControllerITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VetRepository repo;

    @BeforeEach
    @AfterAll
    void cleanup() {
        repo.deleteAll();
    }

    private final Set<Vet.Specialty> specialties = Set.of(
        new Vet.Specialty(ObjectId.get().toHexString(), "Dogs"),
        new Vet.Specialty(ObjectId.get().toHexString(), "Cats")
    );

    @Test
    public void testGetAllVets() throws Exception {
        repo.saveAll(
            List.of(
                new Vet(ObjectId.get().toHexString(), "John", "Doe", specialties),
                new Vet(ObjectId.get().toHexString(), "Jane", "Smith", specialties)
            )
        );

        mockMvc.perform(get("/api/v1/vets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", notNullValue()))
            .andExpect(jsonPath("$[0].firstName", is("John")))
            .andExpect(jsonPath("$[0].lastName", is("Doe")))
            .andExpect(jsonPath("$[0].specialties", hasSize(2)))
            .andExpect(jsonPath("$[1].id", notNullValue()))
            .andExpect(jsonPath("$[1].firstName", is("Jane")))
            .andExpect(jsonPath("$[1].lastName", is("Smith")))
            .andExpect(jsonPath("$[1].specialties", hasSize(2)));
    }

    @Test
    public void testGetVetById() throws Exception {
        var vet = repo.save(new Vet(ObjectId.get().toHexString(), "John", "Doe", specialties));

        mockMvc.perform(get("/api/v1/vets/{id}", vet.id()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.firstName", is("John")))
            .andExpect(jsonPath("$.lastName", is("Doe")))
            .andExpect(jsonPath("$.specialties", hasSize(2)))
            .andExpect(jsonPath("$.specialties[0].id", is(specialties.stream().findFirst().get().id())))
            .andExpect(jsonPath("$.specialties[0].name", is(specialties.stream().findFirst().get().name())))
            .andExpect(jsonPath("$.specialties[1].id", is(specialties.stream().skip(1).findFirst().get().id())))
            .andExpect(jsonPath("$.specialties[1].name", is(specialties.stream().skip(1).findFirst().get().name())));
    }

    @Test
    public void testGetVetById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/vets/{id}", ObjectId.get().toHexString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateVet() throws Exception {
        mockMvc.perform(post("/api/v1/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"firstName":"John","lastName":"Doe", "specialties": []}""")
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.firstName", is("John")))
            .andExpect(jsonPath("$.lastName", is("Doe")))
            .andExpect(jsonPath("$.specialties").isEmpty());
    }

    @Test
    public void testUpdateVet() throws Exception {
        var vet = repo.save(new Vet(ObjectId.get().toHexString(), "John", "Doe", specialties));

        mockMvc.perform(put("/api/v1/vets/{id}", vet.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"firstName":"Jane","lastName":"Smith", "specialties": []}""")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.firstName", is("Jane")))
            .andExpect(jsonPath("$.lastName", is("Smith")))
            .andExpect(jsonPath("$.specialties").isEmpty());
    }

    @Test
    public void testUpdateVet_NotFound() throws Exception {
        mockMvc.perform(put("/api/v1/vets/{id}", ObjectId.get().toHexString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"firstName":"Jane","lastName":"Smith", "specialties": []}""")
            )
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteVet() throws Exception {
        var vet = repo.save(new Vet(ObjectId.get().toHexString(), "John", "Doe", specialties));

        mockMvc.perform(delete("/api/v1/vets/{id}", vet.id()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteVet_NotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/vets/{id}", ObjectId.get().toHexString()))
            .andExpect(status().isNotFound());
    }
}
