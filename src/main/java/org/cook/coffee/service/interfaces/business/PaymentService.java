package org.cook.coffee.service.interfaces.business;

import org.cook.coffee.entity.order.OrderEntity;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    void pay(OrderEntity order);
    void fail(OrderEntity order);
    void refund(OrderEntity order);

}
