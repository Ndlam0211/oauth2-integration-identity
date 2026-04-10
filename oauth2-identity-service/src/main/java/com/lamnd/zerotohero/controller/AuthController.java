package com.lamnd.zerotohero.controller;

import org.springframework.web.bind.annotation.*;

import com.lamnd.zerotohero.dto.reponse.APIResponse;
import com.lamnd.zerotohero.dto.reponse.AuthResponse;
import com.lamnd.zerotohero.dto.reponse.IntrospectResponse;
import com.lamnd.zerotohero.dto.request.AuthRequest;
import com.lamnd.zerotohero.dto.request.IntrospectRequest;
import com.lamnd.zerotohero.dto.request.LogoutRequest;
import com.lamnd.zerotohero.dto.request.RefreshRequest;
import com.lamnd.zerotohero.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/outbound/authenticate")
    public APIResponse<AuthResponse> outboundAuthenticate(@RequestParam("code") String code) {
        AuthResponse authResponse = authService.outboundAuthenticate(code);

        return APIResponse.<AuthResponse>builder().data(authResponse).build();
    }

    @PostMapping("/login")
    public APIResponse<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.authenticate(authRequest);

        return APIResponse.<AuthResponse>builder().data(authResponse).build();
    }

    @PostMapping("/logout")
    public APIResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);

        return APIResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    public APIResponse<AuthResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        AuthResponse authResponse = authService.refreshToken(refreshRequest);

        return APIResponse.<AuthResponse>builder().data(authResponse).build();
    }

    @PostMapping("/introspect")
    public APIResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) {
        IntrospectResponse introspectResponse = authService.introspect(introspectRequest);

        return APIResponse.<IntrospectResponse>builder()
                .code(200)
                .data(introspectResponse)
                .build();
    }
}
