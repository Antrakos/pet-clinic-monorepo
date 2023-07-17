package com.github.antrakos.petclinic.vets.web.dto;

import com.github.antrakos.petclinic.vets.model.Vet;

import java.util.Set;

public record ModifyVetDto(
    String firstName,
    String lastName,
    Set<Vet.Specialty> specialties
) {
    public Vet toModel(String id) {
        return new Vet(id, firstName, lastName, specialties);
    }

    public Vet toModel() {
        return toModel(null);
    }
}
