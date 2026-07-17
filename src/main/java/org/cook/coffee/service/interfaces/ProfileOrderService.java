package org.cook.coffee.service.interfaces;

import org.cook.coffee.model.orderResponse.OrderDetailsResponse;
import org.cook.coffee.model.orderResponse.OrderSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface ProfileOrderService {

    List<OrderSummaryResponse> getUserOrders(UUID userId);
    OrderDetailsResponse getOrderDetails(Long orderId, UUID userId);
    void cancelOrder(Long orderId, UUID userId);

}
