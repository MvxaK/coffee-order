package org.cook.coffee.service.interfaces.order;

import org.cook.coffee.model.order.OrderItemAdditive;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderItemAdditiveService {

    OrderItemAdditive getById(Long id);
    List<OrderItemAdditive> getAll();
    OrderItemAdditive create(OrderItemAdditive orderItemAdditive);
    OrderItemAdditive update(Long id, OrderItemAdditive orderItemAdditive);
    void delete(Long id);

}
