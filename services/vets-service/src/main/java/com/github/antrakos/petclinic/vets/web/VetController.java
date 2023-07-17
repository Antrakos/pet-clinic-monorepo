package com.github.antrakos.petclinic.vets.web;

import com.github.antrakos.petclinic.vets.model.Vet;
import com.github.antrakos.petclinic.vets.repository.VetRepository;
import com.github.antrakos.petclinic.vets.web.dto.ModifyVetDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/vets")
public class VetController {
    private final VetRepository vetRepository;

    public VetController(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    @GetMapping
    public Iterable<Vet> getAllVets() {
        return vetRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vet> getVetById(@PathVariable String id) {
        var vetOptional = vetRepository.findById(id);
        return vetOptional
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vet> createVet(@RequestBody ModifyVetDto vet) {
        var savedVet = vetRepository.save(vet.toModel());
        return ResponseEntity.created(URI.create("/api/v1/vets/" + savedVet.id()))
            .body(savedVet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vet> updateVet(@PathVariable String id, @RequestBody ModifyVetDto body) {
        if (!vetRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var updatedVet = vetRepository.save(body.toModel(id));
        return ResponseEntity.ok(updatedVet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVet(@PathVariable String id) {
        if (!vetRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        vetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
