package org.cook.coffee.service.interfaces.business;

import org.cook.coffee.entity.order.OrderEntity;
import org.springframework.stereotype.Service;

@Service
public interface OrderStatusService {

    void markPaid(OrderEntity order);
    void markPreparing(OrderEntity order);
    void markReady(OrderEntity order);
    void markCompleted(OrderEntity order);
    void cancel(OrderEntity order);

}
