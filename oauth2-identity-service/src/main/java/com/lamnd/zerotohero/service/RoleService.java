package com.lamnd.zerotohero.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lamnd.zerotohero.dto.reponse.RoleResponse;
import com.lamnd.zerotohero.dto.request.RoleRequest;
import com.lamnd.zerotohero.entity.Role;
import com.lamnd.zerotohero.mapper.RoleMapper;
import com.lamnd.zerotohero.repository.PermissionRepo;
import com.lamnd.zerotohero.repository.RoleRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;
    private final RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        var permissions = permissionRepo.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepo.save(role);

        return roleMapper.toDTO(role);
    }

    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        return roleMapper.toListDTO(roles);
    }

    public void deleteRole(String roleName) {
        roleRepo.deleteById(roleName);
    }
}
