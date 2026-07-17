package org.cook.coffee.service.impl.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cook.coffee.entity.AdditiveEntity;
import org.cook.coffee.entity.CoffeeEntity;
import org.cook.coffee.repository.AdditiveRepository;
import org.cook.coffee.repository.CoffeeRepository;
import org.cook.coffee.service.interfaces.business.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final CoffeeRepository coffeeRepository;
    private final AdditiveRepository additiveRepository;

    @Override
    @Transactional
    public void reserveCoffee(CoffeeEntity coffee, int quantity) {
        validateQuantity(quantity);

        int available = coffee.getStock() - coffee.getReservedStock();

        if (available < quantity) {
            throw new IllegalArgumentException("There is not enough coffee in stock");
        }

        coffee.setReservedStock(coffee.getReservedStock() + quantity);
        coffeeRepository.saveAndFlush(coffee);
    }

    @Override
    @Transactional
    public void releaseCoffee(CoffeeEntity coffee, int quantity) {
        validateQuantity(quantity);

        if (coffee.getReservedStock() < quantity) {
            log.info("Coffee stock already released");
            return;
        }

        coffee.setReservedStock(coffee.getReservedStock() - quantity);
        coffeeRepository.saveAndFlush(coffee);
    }

    @Override
    @Transactional
    public void commitCoffee(CoffeeEntity coffee, int quantity) {
        validateQuantity(quantity);

        if (coffee.getReservedStock() < quantity) {
            throw new IllegalArgumentException("Cannot commit stock: reserved stock is less than requested quantity");
        }

        coffee.setReservedStock(coffee.getReservedStock() - quantity);
        coffee.setStock(coffee.getStock() - quantity);
        coffeeRepository.saveAndFlush(coffee);
    }

    @Override
    @Transactional
    public void reserveAdditive(AdditiveEntity additive, int quantity) {
        validateQuantity(quantity);

        int available = additive.getStock() - additive.getReservedStock();

        if (available < quantity) {
            throw new IllegalArgumentException("There is not enough additive in stock");
        }

        additive.setReservedStock(additive.getReservedStock() + quantity);
        additiveRepository.saveAndFlush(additive);
    }

    @Override
    @Transactional
    public void releaseAdditive(AdditiveEntity additive, int quantity) {
        validateQuantity(quantity);

        if (additive.getReservedStock() < quantity) {
            log.info("Additive stock already released");
            return;
        }

        additive.setReservedStock(additive.getReservedStock() - quantity);
        additiveRepository.saveAndFlush(additive);
    }

    @Override
    @Transactional
    public void commitAdditive(AdditiveEntity additive, int quantity) {
        validateQuantity(quantity);

        if (additive.getReservedStock() < quantity) {
            throw new IllegalArgumentException("Cannot commit stock: reserved stock is less than requested quantity");
        }

        additive.setReservedStock(additive.getReservedStock() - quantity);
        additive.setStock(additive.getStock() - quantity);
        additiveRepository.saveAndFlush(additive);
    }

    private void validateQuantity(int quantity){
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

}
