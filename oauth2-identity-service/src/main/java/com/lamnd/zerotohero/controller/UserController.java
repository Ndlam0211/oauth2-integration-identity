package com.lamnd.zerotohero.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lamnd.zerotohero.dto.reponse.APIResponse;
import com.lamnd.zerotohero.dto.reponse.UserResponse;
import com.lamnd.zerotohero.dto.request.UserCreationRequest;
import com.lamnd.zerotohero.dto.request.UserUpdateRequest;
import com.lamnd.zerotohero.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    APIResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse user = userService.createUser(request);

        APIResponse<UserResponse> response =
                APIResponse.<UserResponse>builder().data(user).build();

        return response;
    }

    @GetMapping
    List<UserResponse> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/{userId}")
    ResponseEntity<APIResponse<UserResponse>> getUserById(@PathVariable("userId") String userId) {
        UserResponse user = userService.getUserById(userId);

        APIResponse<UserResponse> response =
                APIResponse.<UserResponse>builder().code(200).data(user).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/myInfo")
    ResponseEntity<APIResponse<UserResponse>> getMyInfo() {
        UserResponse user = userService.getMyInfo();

        APIResponse<UserResponse> response =
                APIResponse.<UserResponse>builder().code(200).data(user).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    ResponseEntity<?> updateUserById(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        UserResponse user = userService.updateUserById(userId, request);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<?> deleteUserById(@PathVariable("userId") String userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>("User Deleted Successfully", HttpStatus.NO_CONTENT);
    }
}
