package org.cook.coffee.controller.api;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.model.Additive;
import org.cook.coffee.service.interfaces.AdditiveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/additives")
@RequiredArgsConstructor
public class AdditiveApiController {

    private final AdditiveService additiveService;

    @GetMapping("/{id}")
    public ResponseEntity<Additive> getById(@PathVariable Long id) {
        return ResponseEntity.ok(additiveService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Additive>> getAll() {
        return ResponseEntity.ok(additiveService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Additive> create(@RequestBody Additive additive) {
        Additive created = additiveService.create(additive);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Additive> update(@PathVariable Long id, @RequestBody Additive additive) {
        return ResponseEntity.ok(additiveService.update(id, additive));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        additiveService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }
}