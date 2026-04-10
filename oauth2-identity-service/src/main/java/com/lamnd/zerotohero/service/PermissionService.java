package com.lamnd.zerotohero.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lamnd.zerotohero.dto.reponse.PermissionResponse;
import com.lamnd.zerotohero.dto.request.PermissionRequest;
import com.lamnd.zerotohero.entity.Permission;
import com.lamnd.zerotohero.mapper.PermissionMapper;
import com.lamnd.zerotohero.repository.PermissionRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepo permissionRepo;
    private final PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepo.save(permission);

        return permissionMapper.toDTO(permission);
    }

    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepo.findAll();
        return permissionMapper.toListDTO(permissions);
    }

    public void deletePermission(String permissionName) {
        permissionRepo.deleteById(permissionName);
    }
}
