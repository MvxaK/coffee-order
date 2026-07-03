package org.cook.coffee.service.interfaces.business;

import org.cook.coffee.model.order.CreateOrderRequest;
import org.cook.coffee.model.order.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderProcessService {

    Order createOrder(CreateOrderRequest request);
    void cancelOrder(Long orderId);
    void confirmPayment(Long orderId);

}
