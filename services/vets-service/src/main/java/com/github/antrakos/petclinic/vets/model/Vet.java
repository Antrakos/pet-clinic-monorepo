package com.github.antrakos.petclinic.vets.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Document(collection = "vets")
public record Vet(
    @MongoId(FieldType.OBJECT_ID) String id,
    @Field("first_name") String firstName,
    @Field("last_name") String lastName,
    Set<Specialty> specialties
) {
    public record Specialty(
        String id,
        String name
    ) {
    }
}
