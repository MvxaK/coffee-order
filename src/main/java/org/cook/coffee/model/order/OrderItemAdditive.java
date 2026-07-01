package org.cook.coffee.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAdditive {

    private Long id;
    private Long orderItemId;
    private Long additiveId;
    private BigDecimal price;
    private Integer quantity;

}
