package com.github.antrakos.petclinic.customers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "owners")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Owner {
    private final @jakarta.persistence.Id Id id;
    private final @Embedded FirstName firstName;
    private final @Embedded LastName lastName;


    @Embeddable
    @Data
    @AllArgsConstructor
    public static class Id implements Serializable {
        private final UUID id;

        public Id() {
            this(UUID.randomUUID());
        }
    }

    @Data
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    public static class FirstName {
        private final @Column(name = "first_name") String name;
    }

    @Data
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    public static class LastName {
        private final @Column(name = "last_name") String name;
    }
}
