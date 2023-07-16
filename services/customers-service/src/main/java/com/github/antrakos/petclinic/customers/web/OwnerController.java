package com.github.antrakos.petclinic.customers.web;

import com.github.antrakos.petclinic.customers.model.Owner;
import com.github.antrakos.petclinic.customers.repository.OwnerRepository;
import com.github.antrakos.petclinic.customers.web.dto.ModifyOwnerDto;
import com.github.antrakos.petclinic.customers.web.dto.OwnerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Transactional
@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerRepository ownerRepository;

    @GetMapping
    public List<OwnerDto> getAllOwners() {
        return StreamSupport.stream(ownerRepository.findAll().spliterator(), false)
            .map(OwnerDto::fromModel)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable UUID id) {
        var ownerOptional = ownerRepository.findById(new Owner.Id(id));
        return ownerOptional
            .map(OwnerDto::fromModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OwnerDto> createOwner(@RequestBody ModifyOwnerDto owner) {
        var id = new Owner.Id();
        var savedOwner = ownerRepository.save(owner.toModel(id));
        return ResponseEntity.created(URI.create("/api/v1/owners/" + id.getId()))
            .body(OwnerDto.fromModel(savedOwner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable UUID id, @RequestBody ModifyOwnerDto body) {
        if (!ownerRepository.existsById(new Owner.Id(id))) {
            return ResponseEntity.notFound().build();
        }

        var updatedOwner = ownerRepository.save(body.toModel(new Owner.Id(id)));
        return ResponseEntity.ok(OwnerDto.fromModel(updatedOwner));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable UUID id) {
        if (!ownerRepository.existsById(new Owner.Id(id))) {
            return ResponseEntity.notFound().build();
        }

        ownerRepository.deleteById(new Owner.Id(id));
        return ResponseEntity.noContent().build();
    }
}
