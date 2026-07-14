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
import org.cook.coffee.model.business.OrderStateResponse;
import org.cook.coffee.model.business.PriceCalculationResult;
import org.cook.coffee.model.order.*;
import org.cook.coffee.repository.UserRepository;
import org.cook.coffee.repository.order.OrderRepository;
import org.cook.coffee.service.interfaces.business.*;
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
    private final OrderStatusService orderStatusService;
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

        List<OrderItemEntity> orderItems = buildOrderItems(order, result);

        order.setOrderItems(orderItems);

        return  orderMapper.toModel(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void reserveStock(Long orderId) {
        OrderEntity order = getOrder(orderId);

        for (OrderItemEntity orderItem: order.getOrderItems()){
            stockService.reserveCoffee(orderItem.getCoffee(), orderItem.getQuantity());

            for (OrderItemAdditiveEntity orderItemAdditive: orderItem.getAdditives()){
                stockService.reserveAdditive(orderItemAdditive.getAdditive(), orderItemAdditive.getQuantity());
            }
        }
    }

    @Override
    @Transactional
    public void commitStock(Long orderId) {
        OrderEntity order = getOrder(orderId);

        for (OrderItemEntity orderItem: order.getOrderItems()){
            stockService.commitCoffee(orderItem.getCoffee(), orderItem.getQuantity());

            for (OrderItemAdditiveEntity orderItemAdditive: orderItem.getAdditives()){
                stockService.commitAdditive(orderItemAdditive.getAdditive(), orderItemAdditive.getQuantity());
            }
        }
    }

    @Override
    @Transactional
    public void releaseStock(Long orderId) {
        OrderEntity order = getOrder(orderId);

        for (OrderItemEntity orderItem: order.getOrderItems()){
            stockService.releaseCoffee(orderItem.getCoffee(), orderItem.getQuantity());

            for (OrderItemAdditiveEntity orderItemAdditive: orderItem.getAdditives()){
                stockService.releaseAdditive(orderItemAdditive.getAdditive(), orderItemAdditive.getQuantity());
            }
        }
    }

    @Override
    @Transactional
    public OrderStateResponse confirmPayment(Long orderId) {
        OrderEntity order = getOrder(orderId);

        if(orderId % 3 == 0){
            paymentService.fail(order);
        }else{
            orderStatusService.markPaid(order);
            paymentService.markPaid(order);
        }

        return saveOrder(order);
    }

    @Override
    @Transactional
    public OrderStateResponse startPreparation(Long orderId) {
        OrderEntity order = getOrder(orderId);

        orderStatusService.markPreparing(order);

        return saveOrder(order);
    }

    @Override
    @Transactional
    public OrderStateResponse markReady(Long orderId) {
        OrderEntity order = getOrder(orderId);

        orderStatusService.markReady(order);

        return saveOrder(order);
    }

    @Override
    @Transactional
    public OrderStateResponse completeOrder(Long orderId) {
        OrderEntity order = getOrder(orderId);

        orderStatusService.markCompleted(order);

        return saveOrder(order);
    }

    @Override
    @Transactional
    public OrderStateResponse cancelOrder(Long orderId) {
        OrderEntity order = getOrder(orderId);

        orderStatusService.cancel(order);
        if(!order.getPaymentStatus().equals(PaymentStatus.FAILED)){
            paymentService.refund(order);
        }

        return saveOrder(order);
    }


    private OrderEntity getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("There is no order with id -> " + orderId));
    }

    private OrderStateResponse saveOrder(OrderEntity order) {
        order.setUpdatedAt(LocalDateTime.now());
        order =  orderRepository.save(order);

        return new OrderStateResponse(order.getId(), order.getOrderStatus(), order.getPaymentStatus());
    }

    private List<OrderItemEntity> buildOrderItems(OrderEntity order, PriceCalculationResult result){
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (OrderItemCalculationResult itemResult: result.getItems()) {
            OrderItemEntity item = new OrderItemEntity();
            item.setCoffee(itemResult.getCoffee());
            item.setCoffeeSize(itemResult.getCoffeeSize());
            item.setCoffeeName(itemResult.getCoffee().getName());
            item.setSizeName(itemResult.getCoffeeSize().getSize());
            item.setQuantity(itemResult.getQuantity());
            item.setPrice(itemResult.getItemPrice());

            List<OrderItemAdditiveEntity> additiveEntities = new ArrayList<>();

            for (OrderItemAdditiveCalculationResult additiveResult: itemResult.getAdditives()) {
                OrderItemAdditiveEntity orderItemAdditive = new OrderItemAdditiveEntity();
                orderItemAdditive.setOrderItem(item);
                orderItemAdditive.setAdditive(additiveResult.getAdditive());
                orderItemAdditive.setAdditiveName(additiveResult.getAdditive().getName());
                orderItemAdditive.setQuantity(additiveResult.getQuantity());
                orderItemAdditive.setPrice(additiveResult.getPrice());

                additiveEntities.add(orderItemAdditive);
            }

            item.setAdditives(additiveEntities);
            item.setOrder(order);
            orderItems.add(item);
        }

        return orderItems;
    }
}
