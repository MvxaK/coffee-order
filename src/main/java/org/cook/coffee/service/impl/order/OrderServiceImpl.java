package org.cook.coffee.service.impl.order;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.UserEntity;
import org.cook.coffee.entity.order.OrderEntity;
import org.cook.coffee.mapper.order.OrderMapper;
import org.cook.coffee.model.order.Order;
import org.cook.coffee.repository.UserRepository;
import org.cook.coffee.repository.order.OrderRepository;
import org.cook.coffee.service.interfaces.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order getById(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no order with id -> " + id));

        return orderMapper.toModel(entity);
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public Order create(Order order) {
        OrderEntity entity = orderMapper.toEntity(order);

        UserEntity user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id -> " + order.getUserId()));

        entity.setUser(user);

        return orderMapper.toModel(orderRepository.save(entity));
    }

    @Override
    @Transactional
    public Order update(Long id, Order order) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no order with id -> " + id));

        UserEntity user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id -> " + order.getUserId()));

        entity.setUser(user);
        entity.setOrderStatus(order.getOrderStatus());
        entity.setPaymentStatus(order.getPaymentStatus());
        entity.setTotalAmount(order.getTotalAmount());

        return orderMapper.toModel(orderRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("There is no order with id -> " + id);
        }

        orderRepository.deleteById(id);
    }

}
