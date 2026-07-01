package org.cook.coffee.service.impl.order;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.AdditiveEntity;
import org.cook.coffee.entity.order.OrderItemAdditiveEntity;
import org.cook.coffee.entity.order.OrderItemEntity;
import org.cook.coffee.mapper.order.OrderItemAdditiveMapper;
import org.cook.coffee.model.order.OrderItemAdditive;
import org.cook.coffee.repository.AdditiveRepository;
import org.cook.coffee.repository.order.OrderItemAdditiveRepository;
import org.cook.coffee.repository.order.OrderItemRepository;
import org.cook.coffee.service.interfaces.order.OrderItemAdditiveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemAdditiveServiceImpl implements OrderItemAdditiveService {

    private final OrderItemAdditiveRepository orderItemAdditiveRepository;
    private final OrderItemRepository orderItemRepository;
    private final AdditiveRepository additiveRepository;
    private final OrderItemAdditiveMapper orderItemAdditiveMapper;

    @Override
    public OrderItemAdditive getById(Long id) {
        OrderItemAdditiveEntity entity = orderItemAdditiveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no order item additive with id -> " + id));

        return orderItemAdditiveMapper.toModel(entity);
    }

    @Override
    public List<OrderItemAdditive> getAll() {
        return orderItemAdditiveRepository.findAll()
                .stream()
                .map(orderItemAdditiveMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public OrderItemAdditive create(OrderItemAdditive orderItemAdditive) {
        OrderItemAdditiveEntity entity = orderItemAdditiveMapper.toEntity(orderItemAdditive);

        OrderItemEntity orderItem = orderItemRepository.findById(orderItemAdditive.getOrderItemId())
                .orElseThrow(() -> new EntityNotFoundException("There is no order item with id -> " + orderItemAdditive.getOrderItemId()));

        AdditiveEntity additive = additiveRepository.findById(orderItemAdditive.getAdditiveId())
                .orElseThrow(() -> new EntityNotFoundException("There is no additive with id -> " + orderItemAdditive.getAdditiveId()));

        entity.setOrderItem(orderItem);
        entity.setAdditive(additive);

        entity.setPrice(additive.getPrice());
        entity.setQuantity(orderItemAdditive.getQuantity());

        return orderItemAdditiveMapper.toModel(orderItemAdditiveRepository.save(entity));
    }

    @Override
    @Transactional
    public OrderItemAdditive update(Long id, OrderItemAdditive orderItemAdditive) {
        OrderItemAdditiveEntity entity = orderItemAdditiveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no order item additive with id -> " + id));

        OrderItemEntity orderItem = orderItemRepository.findById(orderItemAdditive.getOrderItemId())
                .orElseThrow(() -> new EntityNotFoundException("There is no order item with id -> " + orderItemAdditive.getOrderItemId()));

        AdditiveEntity additive = additiveRepository.findById(orderItemAdditive.getAdditiveId())
                .orElseThrow(() -> new EntityNotFoundException("There is no additive with id -> " + orderItemAdditive.getAdditiveId()));

        entity.setOrderItem(orderItem);
        entity.setAdditive(additive);

        entity.setPrice(additive.getPrice());
        entity.setQuantity(orderItemAdditive.getQuantity());

        return orderItemAdditiveMapper.toModel(orderItemAdditiveRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderItemAdditiveRepository.existsById(id)) {
            throw new EntityNotFoundException("There is no order item additive with id -> " + id);
        }

        orderItemAdditiveRepository.deleteById(id);
    }

}