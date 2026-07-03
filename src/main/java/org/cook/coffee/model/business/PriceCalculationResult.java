package org.cook.coffee.model.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceCalculationResult {

    private BigDecimal totalPrice;
    private List<OrderItemCalculationResult> items;

}
