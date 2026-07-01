package org.cook.coffee.mapper;

import org.cook.coffee.entity.CoffeeEntity;
import org.cook.coffee.model.Coffee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoffeeMapper {

    Coffee toModel(CoffeeEntity entity);
    CoffeeEntity toEntity(Coffee model);

}
