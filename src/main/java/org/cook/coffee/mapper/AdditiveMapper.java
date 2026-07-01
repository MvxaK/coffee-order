package org.cook.coffee.mapper;

import org.cook.coffee.entity.AdditiveEntity;
import org.cook.coffee.model.Additive;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdditiveMapper {

    Additive toModel(AdditiveEntity entity);
    AdditiveEntity toEntity(Additive model);

}
