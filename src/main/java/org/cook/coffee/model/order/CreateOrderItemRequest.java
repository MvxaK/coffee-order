package org.cook.coffee.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderItemRequest {

    private Long coffeeId;
    private Long sizeId;
    private int quantity;
    private List<CreateOrderItemAdditiveRequest> requests;

}
