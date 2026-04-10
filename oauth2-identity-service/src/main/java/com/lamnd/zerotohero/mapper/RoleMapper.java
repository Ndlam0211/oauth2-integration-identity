package com.lamnd.zerotohero.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lamnd.zerotohero.dto.reponse.RoleResponse;
import com.lamnd.zerotohero.dto.request.RoleRequest;
import com.lamnd.zerotohero.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toDTO(Role role);

    List<RoleResponse> toListDTO(List<Role> roles);
}
