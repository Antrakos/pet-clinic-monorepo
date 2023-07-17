package com.github.antrakos.petclinic.vets.repository;

import com.github.antrakos.petclinic.vets.model.Vet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VetRepository extends MongoRepository<Vet, String> {
}
