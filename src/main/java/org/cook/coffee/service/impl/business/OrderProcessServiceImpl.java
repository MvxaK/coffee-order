package org.cook.coffee.service.impl.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.UserEntity;
import org.cook.coffee.entity.order.OrderEntity;
import org.cook.coffee.entity.order.OrderItemAdditiveEntity;
import org.cook.coffee.entity.order.OrderItemEntity;
import org.cook.coffee.mapper.order.OrderMapper;
import org.cook.coffee.model.PaymentStatus;
import org.cook.coffee.model.business.OrderItemAdditiveCalculationResult;
import org.cook.coffee.model.business.OrderItemCalculationResult;
import org.cook.coffee.model.business.PriceCalculationResult;
import org.cook.coffee.model.order.*;
import org.cook.coffee.repository.UserRepository;
import org.cook.coffee.repository.order.OrderRepository;
import org.cook.coffee.service.interfaces.business.OrderProcessService;
import org.cook.coffee.service.interfaces.business.PaymentService;
import org.cook.coffee.service.interfaces.business.PriceCalculatorService;
import org.cook.coffee.service.interfaces.business.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProcessServiceImpl implements OrderProcessService {

    private final PaymentService paymentService;
    private final PriceCalculatorService priceCalculatorService;
    private final StockService stockService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id -> " + request.getUserId()));

        PriceCalculationResult result = priceCalculatorService.calculatePrice(request);

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setTotalAmount(result.getTotalPrice());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (OrderItemCalculationResult itemResult : result.getItems()) {
            stockService.reserveCoffee(itemResult.getCoffee(), itemResult.getQuantity());

            OrderItemEntity item = new OrderItemEntity();
            item.setCoffee(itemResult.getCoffee());
            item.setCoffeeSize(itemResult.getCoffeeSize());
            item.setCoffeeName(itemResult.getCoffee().getName());
            item.setSizeName(itemResult.getCoffeeSize().getSize());
            item.setQuantity(itemResult.getQuantity());
            item.setPrice(itemResult.getItemPrice());

            List<OrderItemAdditiveEntity> additiveEntities = new ArrayList<>();

            for (OrderItemAdditiveCalculationResult additiveResult : itemResult.getAdditives()) {
                stockService.reserveAdditive(additiveResult.getAdditive(), additiveResult.getQuantity());

                OrderItemAdditiveEntity orderItemAdditive = new OrderItemAdditiveEntity();
                orderItemAdditive.setOrderItem(item);
                orderItemAdditive.setAdditive(additiveResult.getAdditive());
                orderItemAdditive.setAdditiveName(additiveResult.getAdditive().getName());
                orderItemAdditive.setQuantity(additiveResult.getQuantity());
                orderItemAdditive.setPrice(additiveResult.getPrice());

                additiveEntities.add(orderItemAdditive);
            }

            item.setAdditives(additiveEntities);
            orderItems.add(item);
        }

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        return orderMapper.toModel(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("There is no order with id -> " + orderId));

        paymentService.refund(order);

        for (OrderItemEntity orderItem : order.getOrderItems()){
            stockService.releaseCoffee(orderItem.getCoffee(), orderItem.getQuantity());

            for (OrderItemAdditiveEntity orderItemAdditive: orderItem.getAdditives()){
                stockService.releaseAdditive(orderItemAdditive.getAdditive(), orderItemAdditive.getQuantity());
            }
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void confirmPayment(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("There is no order with id -> " + orderId));

        paymentService.pay(order);

        for (OrderItemEntity orderItem: order.getOrderItems()){
            stockService.commitCoffee(orderItem.getCoffee(), orderItem.getQuantity());

            for (OrderItemAdditiveEntity orderItemAdditive: orderItem.getAdditives()){
                stockService.commitAdditive(orderItemAdditive.getAdditive(), orderItemAdditive.getQuantity());
            }
        }

        orderRepository.save(order);
    }
}
