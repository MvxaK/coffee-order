package org.cook.coffee.controller.api;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.model.CoffeeSize;
import org.cook.coffee.service.interfaces.CoffeeSizeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coffee-sizes")
@RequiredArgsConstructor
public class CoffeeSizeApiController {

    private final CoffeeSizeService coffeeSizeService;

    @GetMapping("/{id}")
    public ResponseEntity<CoffeeSize> getById(@PathVariable Long id) {
        return ResponseEntity.ok(coffeeSizeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CoffeeSize>> getAll() {
        return ResponseEntity.ok(coffeeSizeService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CoffeeSize> create(@RequestBody CoffeeSize coffeeSize) {
        CoffeeSize created = coffeeSizeService.create(coffeeSize);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CoffeeSize> update(@PathVariable Long id, @RequestBody CoffeeSize coffeeSize) {
        return ResponseEntity.ok(coffeeSizeService.update(id, coffeeSize));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coffeeSizeService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }

}
