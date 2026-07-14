package org.cook.coffee.service.interfaces.business;

import org.cook.coffee.model.business.OrderStateResponse;
import org.cook.coffee.model.order.CreateOrderRequest;
import org.cook.coffee.model.order.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderProcessService {

    Order createOrder(CreateOrderRequest request);
    void reserveStock(Long orderId);
    void releaseStock(Long orderId);
    void commitStock(Long orderId);
    OrderStateResponse confirmPayment(Long orderId);
    OrderStateResponse startPreparation(Long orderId);
    OrderStateResponse markReady(Long orderId);
    OrderStateResponse completeOrder(Long orderId);
    OrderStateResponse cancelOrder(Long orderId);

}
