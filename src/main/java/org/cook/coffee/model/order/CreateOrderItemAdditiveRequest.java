package org.cook.coffee.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderItemAdditiveRequest {

    private Long additiveId;
    private int quantity;

}
