package org.cook.coffee.repository;

import org.cook.coffee.entity.CoffeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoffeeRepository extends JpaRepository<CoffeeEntity, Long> {

    @Query("select c from CoffeeEntity c where c.deleted = false")
    List<CoffeeEntity> findAllDeletedFalse();

    Optional<CoffeeEntity> findByIdAndDeletedFalse(Long id);
}
