package org.cook.coffee.mapper;

import org.cook.coffee.entity.UserEntity;
import org.cook.coffee.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toModel(UserEntity entity);

    @Mapping(target = "passwordHash", ignore = true)
    UserEntity toEntity(User model);

}
