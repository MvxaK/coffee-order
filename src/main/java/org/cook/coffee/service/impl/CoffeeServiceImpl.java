package org.cook.coffee.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.CoffeeEntity;
import org.cook.coffee.mapper.CoffeeMapper;
import org.cook.coffee.model.Coffee;
import org.cook.coffee.repository.CoffeeRepository;
import org.cook.coffee.service.interfaces.CoffeeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeServiceImpl implements CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final CoffeeMapper coffeeMapper;

    @Override
    @Cacheable(value = "COFFEE_CACHE", key = "#id")
    public Coffee getById(Long id) {
        System.out.println("SOSOSOOSOSOSOSOSOSOSOOSO");
        CoffeeEntity entity = coffeeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no coffee with id -> " + id));

        return coffeeMapper.toModel(entity);
    }

    @Override
    @Cacheable(value = "COFFEES_CACHE", key = "'all'")
    public List<Coffee> getAll() {
        System.out.println("SOSOSOOSOSOSOSOSOSOSOOSO");
        return coffeeRepository.findAllDeletedFalse()
                .stream()
                .map(coffeeMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    @CachePut(value = "COFFEE_CACHE", key = "#result.getId()")
    public Coffee create(Coffee coffee) {
        CoffeeEntity entity = coffeeMapper.toEntity(coffee);

        return coffeeMapper.toModel(coffeeRepository.save(entity));
    }

    @Override
    @Transactional
    @CachePut(value = "COFFEE_CACHE", key = "#result.getId()")
    public Coffee update(Long id, Coffee coffee) {
        CoffeeEntity entity = coffeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no coffee with id -> " + id));

        entity.setName(coffee.getName());
        entity.setDescription(coffee.getDescription());
        entity.setPrice(coffee.getPrice());
        entity.setStock(coffee.getStock());
        entity.setReservedStock(coffee.getReservedStock());
        entity.setUrl(coffee.getUrl());

        return coffeeMapper.toModel(coffeeRepository.save(entity));
    }

    @Override
    @Transactional
    @CacheEvict(value = "COFFEE_CACHE", key = "#id")
    public void delete(Long id) {
        if (!coffeeRepository.existsById(id)) {
            throw new EntityNotFoundException("There is no coffee with id -> " + id);
        }

        coffeeRepository.deleteById(id);
    }

}
