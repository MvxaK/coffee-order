package org.cook.coffee.service.interfaces.business;

import org.cook.coffee.entity.AdditiveEntity;
import org.cook.coffee.entity.CoffeeEntity;
import org.springframework.stereotype.Service;

@Service
public interface StockService {

    void reserveCoffee(CoffeeEntity coffee, int quantity);
    void releaseCoffee(CoffeeEntity coffee, int quantity);
    void commitCoffee(CoffeeEntity coffee, int quantity);

    void reserveAdditive(AdditiveEntity additive, int quantity);
    void releaseAdditive(AdditiveEntity additive, int quantity);
    void commitAdditive(AdditiveEntity additive, int quantity);

}
