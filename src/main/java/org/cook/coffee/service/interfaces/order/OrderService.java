package org.cook.coffee.service.interfaces.order;

import org.cook.coffee.model.order.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    Order getById(Long id);
    List<Order> getAll();
    Order create(Order order);
    Order update(Long id, Order order);
    void delete(Long id);

}
