package org.cook.coffee.mapper.order;

import org.cook.coffee.entity.order.OrderEntity;
import org.cook.coffee.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "items", source = "orderItems")
    Order toModel(OrderEntity entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    OrderEntity toEntity(Order model);

}