package org.cook.coffee.mapper;

import org.cook.coffee.entity.order.OrderEntity;
import org.cook.coffee.entity.order.OrderItemAdditiveEntity;
import org.cook.coffee.entity.order.OrderItemEntity;
import org.cook.coffee.model.orderResponse.OrderDetailsResponse;
import org.cook.coffee.model.orderResponse.OrderItemAdditiveResponse;
import org.cook.coffee.model.orderResponse.OrderItemDetailsResponse;
import org.cook.coffee.model.orderResponse.OrderSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileOrderMapper {

    @Mapping(target = "itemsCount", expression = "java(order.getOrderItems().size())")
    OrderSummaryResponse toSummaryResponse(OrderEntity order);

    @Mapping(target = "items", source = "orderItems")
    OrderDetailsResponse toDetailsResponse(OrderEntity order);

    @Mapping(target = "additives", source = "additives")
    OrderItemDetailsResponse toItemResponse(OrderItemEntity item);

    OrderItemAdditiveResponse toAdditiveResponse(OrderItemAdditiveEntity additive);

}