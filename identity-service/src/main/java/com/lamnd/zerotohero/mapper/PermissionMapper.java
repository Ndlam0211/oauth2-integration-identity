package com.lamnd.zerotohero.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.lamnd.zerotohero.dto.reponse.PermissionResponse;
import com.lamnd.zerotohero.dto.request.PermissionRequest;
import com.lamnd.zerotohero.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    void updatePermission(@MappingTarget Permission permission, PermissionRequest request);

    PermissionResponse toDTO(Permission permission);

    List<PermissionResponse> toListDTO(List<Permission> permissions);
}
