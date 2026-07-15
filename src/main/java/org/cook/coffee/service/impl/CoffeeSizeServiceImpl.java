package org.cook.coffee.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.CoffeeSizeEntity;
import org.cook.coffee.mapper.CoffeeSizeMapper;
import org.cook.coffee.model.CoffeeSize;
import org.cook.coffee.repository.CoffeeSizeRepository;
import org.cook.coffee.service.interfaces.CoffeeSizeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeSizeServiceImpl implements CoffeeSizeService {

    private final CoffeeSizeRepository coffeeSizeRepository;
    private final CoffeeSizeMapper coffeeSizeMapper;

    @Override
    @Cacheable(value = "COFFEE_SIZE_CACHE", key = "#id")
    public CoffeeSize getById(Long id) {
        CoffeeSizeEntity entity = coffeeSizeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no coffeeSize with id -> " + id));

        return coffeeSizeMapper.toModel(entity);
    }

    @Override
    @Cacheable(value = "COFFEE_SIZES_CACHE", key = "'all'")
    public List<CoffeeSize> getAll() {
        return coffeeSizeRepository.findAll()
                .stream()
                .map(coffeeSizeMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    @Caching(
            put = @CachePut(value = "COFFEE_SIZE_CACHE", key = "#result.getId()"),
            evict = @CacheEvict(value = "COFFEE_SIZES_CACHE", allEntries = true)
    )
    public CoffeeSize create(CoffeeSize coffeeSize) {
        CoffeeSizeEntity entity = coffeeSizeMapper.toEntity(coffeeSize);

        return coffeeSizeMapper.toModel(coffeeSizeRepository.save(entity));
    }

    @Override
    @Transactional
    @Caching(
            put = @CachePut(value = "COFFEE_SIZE_CACHE", key = "#result.getId()"),
            evict = @CacheEvict(value = "COFFEE_SIZES_CACHE", allEntries = true)
    )
    public CoffeeSize update(Long id, CoffeeSize coffeeSize) {
        CoffeeSizeEntity entity = coffeeSizeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no coffeeSize with id -> " + id));

        entity.setSize(coffeeSize.getSize());
        entity.setPrice(coffeeSize.getPrice());

        return coffeeSizeMapper.toModel(coffeeSizeRepository.save(entity));
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "COFFEE_SIZE_CACHE", key = "#id"),
                    @CacheEvict(value = "COFFEE_SIZES_CACHE", allEntries = true)
            }
    )
    public void delete(Long id) {
        if (!coffeeSizeRepository.existsById(id)) {
            throw new EntityNotFoundException("There is no coffeeSize with id -> " + id);
        }

        coffeeSizeRepository.deleteById(id);
    }
}
