package org.cook.coffee.mapper.order;

import org.cook.coffee.entity.order.OrderItemAdditiveEntity;
import org.cook.coffee.mapper.AdditiveMapper;
import org.cook.coffee.mapper.EntityIdUtils;
import org.cook.coffee.mapper.order.OrderItemMapper;
import org.cook.coffee.model.order.OrderItemAdditive;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AdditiveMapper.class, OrderItemMapper.class})
public interface OrderItemAdditiveMapper {

    @Mapping(target = "orderItemId", expression = "java(org.cook.coffee.mapper.EntityIdUtils.extractId(entity.getOrderItem()))")
    @Mapping(target = "additiveId", expression = "java(org.cook.coffee.mapper.EntityIdUtils.extractId(entity.getAdditive()))")
    OrderItemAdditive toModel(OrderItemAdditiveEntity entity);

    @Mapping(target = "orderItem", ignore = true)
    @Mapping(target = "additive", ignore = true)
    OrderItemAdditiveEntity toEntity(OrderItemAdditive model);

}