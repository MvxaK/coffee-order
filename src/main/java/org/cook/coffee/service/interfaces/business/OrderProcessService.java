package org.cook.coffee.service.interfaces.business;

import org.cook.coffee.model.order.CreateOrderRequest;
import org.cook.coffee.model.order.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderProcessService {

    Order createOrder(CreateOrderRequest request);
    void reserveStock(Long orderId);
    void releaseStock(Long orderId);
    void commitStock(Long orderId);
    void confirmPayment(Long orderId);
    void startPreparation(Long orderId);
    void markReady(Long orderId);
    void completeOrder(Long orderId);
    void cancelOrder(Long orderId);

}
