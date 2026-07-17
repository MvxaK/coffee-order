package org.cook.coffee.model.orderResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetailsResponse {

    private String coffeeName;
    private String sizeName;
    private Integer quantity;
    private BigDecimal price;
    private List<OrderItemAdditiveResponse> additives;

}
