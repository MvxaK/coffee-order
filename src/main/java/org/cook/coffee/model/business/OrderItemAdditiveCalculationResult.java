package org.cook.coffee.model.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cook.coffee.entity.AdditiveEntity;
import org.cook.coffee.model.order.CreateOrderItemAdditiveRequest;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAdditiveCalculationResult {

    private CreateOrderItemAdditiveRequest request;
    private AdditiveEntity additive;
    private Integer quantity;
    private BigDecimal price;

}
