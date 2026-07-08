package org.cook.coffee.service.interfaces;

import org.cook.coffee.model.CoffeeSize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CoffeeSizeService {

    CoffeeSize getById(Long id);
    List<CoffeeSize> getAll();
    CoffeeSize create(CoffeeSize coffeeSize);
    CoffeeSize update(Long id, CoffeeSize coffeeSize);
    void delete(Long id);

}
