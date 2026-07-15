package org.cook.coffee.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.AdditiveEntity;
import org.cook.coffee.mapper.AdditiveMapper;
import org.cook.coffee.model.Additive;
import org.cook.coffee.repository.AdditiveRepository;
import org.cook.coffee.service.interfaces.AdditiveService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdditiveServiceImpl implements AdditiveService {

    private final AdditiveRepository additiveRepository;
    private final AdditiveMapper additiveMapper;

    @Override
    @Cacheable(value = "ADDITIVE_CACHE", key = "#id")
    public Additive getById(Long id) {
        AdditiveEntity entity = additiveRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no additive with id -> " + id));

        return additiveMapper.toModel(entity);
    }

    @Override
    @Cacheable(value = "ADDITIVES_CACHE", key = "'all'")
    public List<Additive> getAll() {
        return additiveRepository.findAllDeletedFalse()
                .stream()
                .map(additiveMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    @Caching(
            put = @CachePut(value = "ADDITIVE_CACHE", key = "#result.id"),
            evict = @CacheEvict(value = "ADDITIVES_CACHE", allEntries = true)
    )
    public Additive create(Additive additive) {
        AdditiveEntity entity = additiveMapper.toEntity(additive);

        return additiveMapper.toModel(additiveRepository.save(entity));
    }

    @Override
    @Transactional
    @Caching(
            put = @CachePut(value = "ADDITIVE_CACHE", key = "#result.id"),
            evict = @CacheEvict(value = "ADDITIVES_CACHE", allEntries = true)
    )
    public Additive update(Long id, Additive additive) {
        AdditiveEntity entity = additiveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no additive with id -> " + id));

        entity.setName(additive.getName());
        entity.setPrice(additive.getPrice());
        entity.setStock(additive.getStock());
        entity.setReservedStock(additive.getReservedStock());
        entity.setUrl(additive.getUrl());

        return additiveMapper.toModel(additiveRepository.save(entity));
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "ADDITIVE_CACHE", key = "#id"),
                    @CacheEvict(value = "ADDITIVES_CACHE", allEntries = true)
            }
    )
    public void delete(Long id) {
        if (!additiveRepository.existsById(id)) {
            throw new EntityNotFoundException("There is no additive with id -> " + id);
        }

        additiveRepository.deleteById(id);
    }

}