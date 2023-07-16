package com.github.antrakos.petclinic.customers.repository;

import com.github.antrakos.petclinic.customers.model.Owner;
import org.springframework.data.repository.CrudRepository;

public interface OwnerRepository extends CrudRepository<Owner, Owner.Id> {
}
