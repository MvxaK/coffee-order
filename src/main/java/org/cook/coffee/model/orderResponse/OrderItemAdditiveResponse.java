package org.cook.coffee.model.orderResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAdditiveResponse {

    private String additiveName;
    private Integer quantity;
    private BigDecimal price;

}