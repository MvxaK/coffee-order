package org.cook.coffee.service.impl.business;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.order.OrderEntity;
import org.cook.coffee.model.PaymentStatus;
import org.cook.coffee.model.order.OrderStatus;
import org.cook.coffee.repository.order.OrderRepository;
import org.cook.coffee.service.interfaces.business.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void pay(OrderEntity order) {
        if(order.getPaymentStatus().equals(PaymentStatus.PAID)){
            throw new IllegalArgumentException("Order is already paid");
        }

        if(order.getPaymentStatus().equals(PaymentStatus.REFUNDED)){
            throw new IllegalArgumentException("Order is refunded cannot pay for it");
        }


        order.setPaymentStatus(PaymentStatus.PAID);
        order.setOrderStatus(OrderStatus.PAID);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void fail(OrderEntity order) {
        order.setPaymentStatus(PaymentStatus.FAILED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void refund(OrderEntity order) {
        if(order.getPaymentStatus().equals(PaymentStatus.REFUNDED)){
            throw new IllegalArgumentException("Order is refunded cannot pay for it");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.REFUNDED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

}
