package org.cook.coffee.repository;

import org.cook.coffee.entity.AdditiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditiveRepository extends JpaRepository<AdditiveEntity, Long> {
}
