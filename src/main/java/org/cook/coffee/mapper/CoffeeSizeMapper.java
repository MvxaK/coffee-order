package org.cook.coffee.mapper;

import org.cook.coffee.entity.CoffeeSizeEntity;
import org.cook.coffee.model.CoffeeSize;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoffeeSizeMapper {

    CoffeeSize toModel(CoffeeSizeEntity entity);
    CoffeeSizeEntity toEntity(CoffeeSize coffeeSize);

}
