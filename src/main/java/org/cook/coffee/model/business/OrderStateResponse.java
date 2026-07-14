package org.cook.coffee.model.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.coffee.model.PaymentStatus;
import org.cook.coffee.model.order.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStateResponse {

    private Long orderId;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;

}
