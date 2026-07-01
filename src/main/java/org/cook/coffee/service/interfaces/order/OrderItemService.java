package org.cook.coffee.service.interfaces.order;

import org.cook.coffee.model.order.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderItemService {

    OrderItem getById(Long id);
    List<OrderItem> getAll();
    OrderItem create(OrderItem orderItem);
    OrderItem update(Long id, OrderItem orderItem);
    void delete(Long id);

}
