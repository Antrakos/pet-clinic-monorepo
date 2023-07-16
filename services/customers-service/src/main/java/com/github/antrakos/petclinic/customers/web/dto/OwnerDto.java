package com.github.antrakos.petclinic.customers.web.dto;

import com.github.antrakos.petclinic.customers.model.Owner;

import java.util.UUID;

public record OwnerDto(
    UUID id,
    String firstName,
    String lastName
) {
    public static OwnerDto fromModel(Owner owner) {
        return new OwnerDto(owner.getId().getId(), owner.getFirstName().getName(), owner.getLastName().getName());
    }
}
