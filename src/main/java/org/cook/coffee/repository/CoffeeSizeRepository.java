package org.cook.coffee.repository;

import org.cook.coffee.entity.CoffeeSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeSizeRepository extends JpaRepository<CoffeeSizeEntity, Long> {
}
