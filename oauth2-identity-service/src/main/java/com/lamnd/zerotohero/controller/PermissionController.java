package com.lamnd.zerotohero.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.lamnd.zerotohero.dto.reponse.APIResponse;
import com.lamnd.zerotohero.dto.reponse.PermissionResponse;
import com.lamnd.zerotohero.dto.request.PermissionRequest;
import com.lamnd.zerotohero.service.PermissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    APIResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionRequest request) {
        PermissionResponse permission = permissionService.createPermission(request);

        return APIResponse.<PermissionResponse>builder().data(permission).build();
    }

    @GetMapping
    List<PermissionResponse> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @DeleteMapping("/{permission}")
    APIResponse<Void> deletePermission(@PathVariable("permission") String permissionName) {
        permissionService.deletePermission(permissionName);

        return APIResponse.<Void>builder()
                .message("Deleted permission successfully")
                .build();
    }
}
