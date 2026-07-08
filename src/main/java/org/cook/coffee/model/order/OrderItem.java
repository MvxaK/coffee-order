package org.cook.coffee.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private Long id;
    private Long orderId;
    private Long coffeeId;
    private Long sizeId;
    private String coffeeName;
    private String sizeName;
    private int quantity;
    private List<Long> additiveIds;
    private BigDecimal price;

}
