package org.cook.coffee.service.impl.order;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.CoffeeEntity;
import org.cook.coffee.entity.CoffeeSizeEntity;
import org.cook.coffee.entity.order.OrderItemEntity;
import org.cook.coffee.mapper.order.OrderItemMapper;
import org.cook.coffee.model.order.OrderItem;
import org.cook.coffee.repository.CoffeeRepository;
import org.cook.coffee.repository.CoffeeSizeRepository;
import org.cook.coffee.repository.order.OrderItemRepository;
import org.cook.coffee.service.interfaces.order.OrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final CoffeeRepository coffeeRepository;
    private final CoffeeSizeRepository coffeeSizeRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItem getById(Long id) {
        OrderItemEntity entity = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no order item with id -> " + id));

        return orderItemMapper.toModel(entity);
    }

    @Override
    public List<OrderItem> getAll() {
        return orderItemRepository.findAll()
                .stream()
                .map(orderItemMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public OrderItem create(OrderItem orderItem) {
        OrderItemEntity entity = orderItemMapper.toEntity(orderItem);

        CoffeeEntity coffee = coffeeRepository.findById(orderItem.getCoffeeId())
                .orElseThrow(() -> new EntityNotFoundException("There is no coffee with id -> " + orderItem.getCoffeeId()));

        CoffeeSizeEntity coffeeSize = coffeeSizeRepository.findById(orderItem.getSizeId())
                .orElseThrow(() -> new EntityNotFoundException("There is no coffee size with id -> " + orderItem.getSizeId()));

        entity.setCoffee(coffee);
        entity.setCoffeeSize(coffeeSize);
        entity.setSizeName(coffeeSize.getSize());

        return orderItemMapper.toModel(orderItemRepository.save(entity));
    }

    @Override
    @Transactional
    public OrderItem update(Long id, OrderItem orderItem) {
        OrderItemEntity entity = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no order item with id -> " + id));

        CoffeeEntity coffee = coffeeRepository.findById(orderItem.getCoffeeId())
                .orElseThrow(() -> new EntityNotFoundException("There is no coffee with id -> " + orderItem.getCoffeeId()));

        CoffeeSizeEntity coffeeSize = coffeeSizeRepository.findById(orderItem.getSizeId())
                .orElseThrow(() -> new EntityNotFoundException("There is no coffee size with id -> " + orderItem.getSizeId()));

        entity.setCoffee(coffee);
        entity.setCoffeeName(orderItem.getCoffeeName());

        entity.setCoffeeSize(coffeeSize);
        entity.setSizeName(coffeeSize.getSize());

        entity.setQuantity(orderItem.getQuantity());
        entity.setPrice(orderItem.getPrice());

        return orderItemMapper.toModel(orderItemRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new EntityNotFoundException("There is no order item with id -> " + id);
        }

        orderItemRepository.deleteById(id);
    }

}
