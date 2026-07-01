package org.cook.coffee.repository.order;

import org.cook.coffee.entity.order.OrderItemAdditiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemAdditiveRepository extends JpaRepository<OrderItemAdditiveEntity, Long> {
}
