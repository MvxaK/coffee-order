package org.cook.coffee.mapper;

import org.cook.coffee.entity.CoffeeEntity;
import org.cook.coffee.model.Coffee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoffeeMapper {

    Coffee toModel(CoffeeEntity entity);

    @Mapping(target = "version", ignore = true)
    CoffeeEntity toEntity(Coffee model);

}
