package org.cook.coffee.service.impl.business;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.order.OrderEntity;
import org.cook.coffee.model.OrderStatus;
import org.cook.coffee.service.interfaces.business.OrderStatusService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

    @Override
    public void markPaid(OrderEntity order) {
        if(!order.getOrderStatus().equals(OrderStatus.CREATED)){
            throw new IllegalArgumentException("Order is not created");
        }

        order.setOrderStatus(OrderStatus.PAID);
    }

    @Override
    public void markPreparing(OrderEntity order) {
        if(!order.getOrderStatus().equals(OrderStatus.PAID)){
            throw new IllegalArgumentException("Order is not paid");
        }

        order.setOrderStatus(OrderStatus.PREPARING);
    }

    @Override
    public void markReady(OrderEntity order) {
        if(!order.getOrderStatus().equals(OrderStatus.PREPARING)){
            throw new IllegalArgumentException("Order is not preparing");
        }

        order.setOrderStatus(OrderStatus.READY);
    }

    @Override
    public void markCompleted(OrderEntity order) {
        if(!order.getOrderStatus().equals(OrderStatus.READY)){
            throw new IllegalArgumentException("Order is not ready");
        }

        order.setOrderStatus(OrderStatus.COMPLETED);
    }

    @Override
    public void cancel(OrderEntity order) {
        if(order.getOrderStatus().equals(OrderStatus.COMPLETED)){
            throw new IllegalArgumentException("Order is completed, cannot cancel");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
    }
}
