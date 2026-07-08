package org.cook.coffee.mapper.order;

import org.cook.coffee.entity.order.OrderItemEntity;
import org.cook.coffee.mapper.CoffeeMapper;
import org.cook.coffee.mapper.CoffeeSizeMapper;
import org.cook.coffee.mapper.EntityIdUtils;
import org.cook.coffee.model.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CoffeeMapper.class, CoffeeSizeMapper.class})
public interface OrderItemMapper {

    @Mapping(target = "coffeeId", expression = "java(org.cook.coffee.mapper.EntityIdUtils.extractId(entity.getCoffee()))")
    @Mapping(target = "sizeId", expression = "java(org.cook.coffee.mapper.EntityIdUtils.extractId(entity.getCoffeeSize()))")
    @Mapping(target = "orderId", expression = "java(org.cook.coffee.mapper.EntityIdUtils.extractId(entity.getOrder()))")
    @Mapping(target = "additiveIds", expression = "java(org.cook.coffee.mapper.EntityIdUtils.extractIds(entity.getAdditives().stream().map(org.cook.coffee.entity.order.OrderItemAdditiveEntity::getAdditive).toList()))")
    OrderItem toModel(OrderItemEntity entity);

    @Mapping(target = "coffee", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "coffeeSize", ignore = true)
    @Mapping(target = "sizeName", ignore = true)
    @Mapping(target = "additives", ignore = true)
    OrderItemEntity toEntity(OrderItem model);

}
