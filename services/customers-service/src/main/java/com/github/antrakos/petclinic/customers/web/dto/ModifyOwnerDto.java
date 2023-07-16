package com.github.antrakos.petclinic.customers.web.dto;

import com.github.antrakos.petclinic.customers.model.Owner;

public record ModifyOwnerDto(
    String firstName,
    String lastName
) {
    public Owner toModel(Owner.Id id) {
        return new Owner(id, new Owner.FirstName(firstName), new Owner.LastName(lastName));
    }
}
