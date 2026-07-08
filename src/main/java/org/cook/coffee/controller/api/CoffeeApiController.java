package org.cook.coffee.controller.api;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.model.Coffee;
import org.cook.coffee.service.interfaces.CoffeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coffees")
@RequiredArgsConstructor
public class CoffeeApiController {

    private final CoffeeService coffeeService;

    @GetMapping("/{id}")
    public ResponseEntity<Coffee> getById(@PathVariable Long id) {
        return ResponseEntity.ok(coffeeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Coffee>> getAll() {
        return ResponseEntity.ok(coffeeService.getAll());
    }

    @PostMapping
    public ResponseEntity<Coffee> create(@RequestBody Coffee coffee) {
        Coffee created = coffeeService.create(coffee);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coffee> update(@PathVariable Long id, @RequestBody Coffee coffee) {
        return ResponseEntity.ok(coffeeService.update(id, coffee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coffeeService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }
}