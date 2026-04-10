package com.lamnd.zerotohero.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.lamnd.zerotohero.dto.reponse.APIResponse;
import com.lamnd.zerotohero.dto.reponse.RoleResponse;
import com.lamnd.zerotohero.dto.request.RoleRequest;
import com.lamnd.zerotohero.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    APIResponse<RoleResponse> createRole(@RequestBody @Valid RoleRequest request) {
        RoleResponse role = roleService.createRole(request);

        return APIResponse.<RoleResponse>builder().data(role).build();
    }

    @GetMapping
    List<RoleResponse> getAllRoles() {
        return roleService.getAllRoles();
    }

    @DeleteMapping("/{role}")
    APIResponse<Void> deleteRole(@PathVariable("role") String role) {
        roleService.deleteRole(role);

        return APIResponse.<Void>builder().message("Deleted role successfully").build();
    }
}
