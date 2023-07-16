package com.github.antrakos.petclinic.customers.web;

import com.github.antrakos.petclinic.customers.IntegrationTest;
import com.github.antrakos.petclinic.customers.model.Owner;
import com.github.antrakos.petclinic.customers.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor
public class OwnerControllerITest {
    private final MockMvc mockMvc;
    private final OwnerRepository repo;

    @Test
    public void testGetAllOwners() throws Exception {
        repo.saveAll(
            List.of(
                new Owner(new Owner.Id(), new Owner.FirstName("John"), new Owner.LastName("Doe")),
                new Owner(new Owner.Id(), new Owner.FirstName("Jane"), new Owner.LastName("Smith"))
            )
        );

        mockMvc.perform(get("/api/v1/owners"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", notNullValue()))
            .andExpect(jsonPath("$[0].firstName", is("John")))
            .andExpect(jsonPath("$[0].lastName", is("Doe")))
            .andExpect(jsonPath("$[1].id", notNullValue()))
            .andExpect(jsonPath("$[1].firstName", is("Jane")))
            .andExpect(jsonPath("$[1].lastName", is("Smith")));
    }

    @Test
    public void testGetOwnerById() throws Exception {
        var owner = repo.save(new Owner(new Owner.Id(), new Owner.FirstName("John"), new Owner.LastName("Doe")));

        mockMvc.perform(get("/api/v1/owners/{id}", owner.getId().getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.firstName", is("John")))
            .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    public void testGetOwnerById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/owners/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOwner() throws Exception {
        mockMvc.perform(post("/api/v1/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"firstName":"John","lastName":"Doe"}""")
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.firstName", is("John")))
            .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    public void testUpdateOwner() throws Exception {
        var owner = repo.save(new Owner(new Owner.Id(), new Owner.FirstName("John"), new Owner.LastName("Doe")));

        mockMvc.perform(put("/api/v1/owners/{id}", owner.getId().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"firstName":"Jane","lastName":"Smith"}""")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.firstName", is("Jane")))
            .andExpect(jsonPath("$.lastName", is("Smith")));
    }

    @Test
    public void testUpdateOwner_NotFound() throws Exception {
        mockMvc.perform(put("/api/v1/owners/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"firstName":"Jane","lastName":"Smith"}""")
            )
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOwner() throws Exception {
        var owner = repo.save(new Owner(new Owner.Id(), new Owner.FirstName("John"), new Owner.LastName("Doe")));

        mockMvc.perform(delete("/api/v1/owners/{id}", owner.getId().getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteOwner_NotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/owners/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }
}
