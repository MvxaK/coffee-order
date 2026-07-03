package org.cook.coffee.model.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.coffee.entity.CoffeeEntity;
import org.cook.coffee.entity.CoffeeSizeEntity;
import org.cook.coffee.model.order.CreateOrderItemRequest;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemCalculationResult {

    private CreateOrderItemRequest request;
    private CoffeeEntity coffee;
    private CoffeeSizeEntity coffeeSize;
    private BigDecimal itemPrice;
    private Integer quantity;

    private List<OrderItemAdditiveCalculationResult> additives;

}
