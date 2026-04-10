package com.lamnd.zerotohero.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lamnd.zerotohero.entity.Role;
import com.lamnd.zerotohero.enums.ERole;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lamnd.zerotohero.dto.reponse.UserResponse;
import com.lamnd.zerotohero.dto.request.UserCreationRequest;
import com.lamnd.zerotohero.dto.request.UserUpdateRequest;
import com.lamnd.zerotohero.entity.User;
import com.lamnd.zerotohero.exception.ResourceExistedException;
import com.lamnd.zerotohero.exception.ResourceNotFoundException;
import com.lamnd.zerotohero.mapper.UserMapper;
import com.lamnd.zerotohero.repository.RoleRepo;
import com.lamnd.zerotohero.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        User user = userMapper.toUser(request);

        Set<Role> roles = new HashSet<>();
        roleRepo.findById(ERole.USER.name()).map((roles::add));
        user.setRoles(roles);

        try {
            user = userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceExistedException("User", "username", request.getUsername());
        }

        return userMapper.toDTO(user);
    }

    @PreAuthorize("hasRole('ADMIN')") // only admin is able to access
    public List<UserResponse> getAllUser() {
        return userMapper.toListDTO(userRepo.findAll());
    }

    // users can only get their own info, admin can get any user info
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public UserResponse getUserById(String userId) {
        return userMapper.toDTO(findUserById(userId));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();

        String username = context.getAuthentication().getName();

        User user = findUserByUsername(username);

        return userMapper.toDTO(user);
    }

    public UserResponse updateUserById(String userId, UserUpdateRequest request) {
        User user = findUserById(userId);

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepo.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toDTO(userRepo.save(user));
    }

    public void deleteUserById(String userId) {
        userRepo.deleteById(userId);
    }

    private User findUserById(String userId) {
        return userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private User findUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
