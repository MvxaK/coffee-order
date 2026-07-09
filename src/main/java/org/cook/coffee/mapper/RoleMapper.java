package org.cook.coffee.mapper;

import org.cook.coffee.entity.RoleEntity;
import org.cook.coffee.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    default Role toModel(RoleEntity entity){
        if(entity == null)
            return null;

        return entity.getRole();
    }

    default RoleEntity toEntity(Role role){
        if(role == null)
            return null;

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole(role);

        return roleEntity;
    }

}
