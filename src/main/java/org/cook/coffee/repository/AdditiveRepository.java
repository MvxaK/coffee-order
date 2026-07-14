package org.cook.coffee.repository;

import org.cook.coffee.entity.AdditiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdditiveRepository extends JpaRepository<AdditiveEntity, Long> {

    @Query("select a from AdditiveEntity a where a.deleted = false")
    List<AdditiveEntity> findAllDeletedFalse();

    Optional<AdditiveEntity> findByIdAndDeletedFalse(Long id);
}
