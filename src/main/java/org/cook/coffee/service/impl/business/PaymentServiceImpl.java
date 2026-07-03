package org.cook.coffee.service.impl.business;

import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.order.OrderEntity;
import org.cook.coffee.model.PaymentStatus;
import org.cook.coffee.model.order.OrderStatus;
import org.cook.coffee.service.interfaces.business.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Override
    public void markPaid(OrderEntity order) {
        if(order.getPaymentStatus().equals(PaymentStatus.PAID)){
            throw new IllegalArgumentException("Order is already paid");
        }

        if(order.getPaymentStatus().equals(PaymentStatus.REFUNDED)){
            throw new IllegalArgumentException("Order is refunded cannot pay for it");
        }

        order.setPaymentStatus(PaymentStatus.PAID);
    }

    @Override
    public void fail(OrderEntity order) {
        order.setPaymentStatus(PaymentStatus.FAILED);
    }

    @Override
    public void refund(OrderEntity order) {
        if(order.getPaymentStatus().equals(PaymentStatus.REFUNDED)){
            throw new IllegalArgumentException("Order is refunded cannot pay for it");
        }

        order.setPaymentStatus(PaymentStatus.REFUNDED);
    }
}
