package org.cook.coffee.mapper;

import org.cook.coffee.entity.AdditiveEntity;
import org.cook.coffee.model.Additive;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdditiveMapper {

    Additive toModel(AdditiveEntity entity);

    @Mapping(target = "version", ignore = true)
    AdditiveEntity toEntity(Additive model);

}
