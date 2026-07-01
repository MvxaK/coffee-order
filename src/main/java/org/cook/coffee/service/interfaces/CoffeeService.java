package org.cook.coffee.service.interfaces;

import org.cook.coffee.model.Coffee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CoffeeService {

    Coffee getById(Long id);
    List<Coffee> getAll();
    Coffee create(Coffee coffee);
    Coffee update(Long id, Coffee coffee);
    void delete(Long id);

}
