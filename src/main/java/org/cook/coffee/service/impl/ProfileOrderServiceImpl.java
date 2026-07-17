package org.cook.coffee.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.order.OrderEntity;
import org.cook.coffee.mapper.ProfileOrderMapper;
import org.cook.coffee.model.OrderStatus;
import org.cook.coffee.model.orderResponse.OrderDetailsResponse;
import org.cook.coffee.model.orderResponse.OrderSummaryResponse;
import org.cook.coffee.repository.order.OrderRepository;
import org.cook.coffee.service.interfaces.camunda.CamundaClientService;
import org.cook.coffee.service.interfaces.ProfileOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileOrderServiceImpl implements ProfileOrderService {

    private final OrderRepository orderRepository;
    private final ProfileOrderMapper profileOrderMapper;
    private final CamundaClientService camundaClientService;

    @Override
    public List<OrderSummaryResponse> getUserOrders(UUID userId) {
        return orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(profileOrderMapper::toSummaryResponse)
                .toList();
    }

    @Override
    public OrderDetailsResponse getOrderDetails(Long orderId, UUID userId) {
        OrderEntity order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        return profileOrderMapper.toDetailsResponse(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, UUID userId) {
        OrderEntity order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if(order.getOrderStatus().equals(OrderStatus.CANCELLED) || order.getOrderStatus().equals(OrderStatus.COMPLETED) || order.getOrderStatus().equals(OrderStatus.READY)){
            throw new IllegalArgumentException("Can not cancel order that is " + order.getOrderStatus().name());
        }

        camundaClientService.cancelOrderProcess(order.getId());
    }

}
